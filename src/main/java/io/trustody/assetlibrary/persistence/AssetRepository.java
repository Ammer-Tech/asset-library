package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.ledger.entities.assets.BaseAsset;
import dev.morphia.Datastore;
import io.trustody.assetlibrary.AssetServer;

import java.util.List;
import java.util.UUID;

public interface AssetRepository<T extends BaseAsset> {
    Datastore datastore  = AssetServer.datastore;
    List<T> listElements(UUID id);
    T upsertElement(T element);
    boolean deleteElement(T element);
}
