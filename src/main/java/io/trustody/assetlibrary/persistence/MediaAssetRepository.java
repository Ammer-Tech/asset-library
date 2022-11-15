package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.ledger.entities.assets.MediaAsset;
import ammer.tech.commons.ledger.events.AssetChangeEvent;
import com.jsoniter.output.JsonStream;
import dev.morphia.query.experimental.filters.Filters;
import io.trustody.assetlibrary.incremental.EventQueueController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Setter;
import org.bouncycastle.asn1.DERSequence;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MediaAssetRepository implements AssetRepository<MediaAsset> {

    @Inject
    @Setter
    private EventQueueController eventQueueController;

    @Override
    public List<MediaAsset> listElements(UUID id) {
        var f = Filters.eq("parent", id);
        return datastore.find(MediaAsset.class).filter(f).iterator().toList();
    }

    @Override
    public List<MediaAsset> listElements() {
        return null;
    }

    @Override
    public MediaAsset upsertElement(MediaAsset element) {
        if(element.getId() == null) element.setId(UUID.randomUUID());
        var x = datastore.save(element);
        try {
            eventQueueController.storeChangeEvent(AssetChangeEvent.builder()
                    .networkChange(false).codecType(x.getAssetType())
                    .objectId(element.getId()).deleted(false).changeData(new DERSequence(x.encodeToVector()).getEncoded()).build()
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return x;
    }

    @Override
    public boolean deleteElement(MediaAsset element) {
        if(datastore.delete(element).getDeletedCount() == 1){
            eventQueueController.storeChangeEvent(AssetChangeEvent.builder()
                    .networkChange(false).codecType(element.getAssetType())
                    .objectId(element.getId()).deleted(true).changeData(null).build()
            );
            return true;
        }
        return false;
    }
}
