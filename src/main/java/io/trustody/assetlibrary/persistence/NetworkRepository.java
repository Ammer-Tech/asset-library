package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.ledger.entities.assets.Network;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NetworkRepository implements AssetRepository<Network> {

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
        return datastore.save(element);
    }

    @Override
    public boolean deleteElement(Network element) {
        return datastore.delete(element).getDeletedCount() == 1;
    }
}
