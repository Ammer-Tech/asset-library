package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.SmartAsset;
import com.jsoniter.output.JsonStream;
import dev.morphia.query.experimental.filters.Filters;
import io.trustody.assetlibrary.incremental.ChangeEvent;
import io.trustody.assetlibrary.incremental.EventQueueController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class SmartAssetRepository implements AssetRepository<SmartAsset> {

    @Inject
    private EventQueueController eventQueueController;
    @Override
    public List<SmartAsset> listElements(UUID id) {
        var f = Filters.and(
                Filters.eq("parent", id),
                Filters.ne("assetType", CodecTypes.NATIVE)
        );

        return datastore.find("assets", Object.class).disableValidation()
                .filter(f).iterator().toList().stream()
                .map(o -> (SmartAsset) o).collect(Collectors.toList());
    }

    @Override
    public List<SmartAsset> listElements() {
        return null;
    }

    @Override
    public SmartAsset upsertElement(SmartAsset element) {
        if (element.getId() == null) element.setId(UUID.randomUUID());
        var x = datastore.save(element);
        eventQueueController.storeChangeEvent(ChangeEvent.builder()
                .networkChange(false).codecType(x.getAssetType())
                .objectId(element.getId()).deleted(false).changeData(JsonStream.serialize(x)).build()
        );
        return x;
    }

    @Override
    public boolean deleteElement(SmartAsset element) {
        if(datastore.delete(element).getDeletedCount() == 1){
            eventQueueController.storeChangeEvent(ChangeEvent.builder()
                    .networkChange(false).codecType(element.getAssetType())
                    .objectId(element.getId()).deleted(true).changeData(null).build()
            );
            return true;
        }
        return false;
    }
}
