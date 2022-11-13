package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.ledger.entities.assets.Network;
import ammer.tech.commons.ledger.events.AssetChangeEvent;
import com.jsoniter.output.JsonStream;
import io.trustody.assetlibrary.incremental.EventQueueController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NetworkRepository implements AssetRepository<Network> {

    @Inject
    @Setter
    private EventQueueController eventQueueController;
    @Override
    public List<Network> listElements(UUID id) {
        return null;
    }

    @Override
    public List<Network> listElements() {
        return datastore.find(Network.class).iterator().toList();
    }

    @Override
    public Network upsertElement(Network element) {
        if(element.getId() == null) element.setId(UUID.randomUUID());
        var x = datastore.save(element);
        eventQueueController.storeChangeEvent(AssetChangeEvent.builder()
                .networkChange(true).codecType(null)
                .objectId(element.getId()).deleted(false).changeData(JsonStream.serialize(x)).build()
        );
        return x;
    }

    @Override
    public boolean deleteElement(Network element) {
        if(datastore.delete(element).getDeletedCount() == 1){
            eventQueueController.storeChangeEvent(AssetChangeEvent.builder()
                    .networkChange(true).codecType(null)
                    .objectId(element.getId()).deleted(true).changeData(null).build()
            );
            return true;
        }
        return false;
    }
}
