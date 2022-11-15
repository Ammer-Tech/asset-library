package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.BaseAsset;
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
public class BaseAssetRepository implements AssetRepository<BaseAsset> {

    @Inject
    @Setter
    private EventQueueController eventQueueController;
    @Override
    public List<BaseAsset> listElements(UUID id) {
        var f = Filters.and(Filters.eq("parent",id),Filters.eq("assetType", CodecTypes.NATIVE));
        return datastore.find(BaseAsset.class).filter(f).iterator().toList();
    }

    @Override
    public List<BaseAsset> listElements() {
        return null;
    }

    @Override
    public BaseAsset upsertElement(BaseAsset element) {
        if(element.getId() == null) element.setId(UUID.randomUUID());
        var x = datastore.save(element);
        try {
            eventQueueController.storeChangeEvent(AssetChangeEvent.builder()
                    .networkChange(false).codecType(CodecTypes.NATIVE)
                    .objectId(element.getId()).deleted(false).changeData(new DERSequence(x.encodeToVector()).getEncoded()).build()
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return x;
    }

    @Override
    public boolean deleteElement(BaseAsset element) {
        if(datastore.delete(element).getDeletedCount() == 1){
            eventQueueController.storeChangeEvent(AssetChangeEvent.builder()
                    .networkChange(false).codecType(CodecTypes.NATIVE)
                    .objectId(element.getId()).deleted(true).changeData(null).build()
            );
            return true;
        }
        return false;
    }
}
