package io.trustody.assetlibrary.persistence;

import dev.morphia.Datastore;
import io.trustody.assetlibrary.AssetServer;

import java.util.List;
import java.util.UUID;

public interface AssetRepository<T> {
    Datastore datastore  = AssetServer.datastore;
    List<T> listElements(UUID id);
    List<T> listElements();
    T upsertElement(T element);
    boolean deleteElement(T element);
}
