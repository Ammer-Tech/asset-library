package io.trustody.assetlibrary.persistence;

import dev.morphia.Datastore;
import io.trustody.assetlibrary.AssetServer;
import io.trustody.assetlibrary.models.SearchRequest;

import java.util.List;

public interface AssetRepository<T> {
    Datastore datastore  = AssetServer.datastore;
    List<T> searchRepository(SearchRequest searchRequest);
    List<T> listElements();
    T upsertElement(T element);
    boolean deleteElement(T element);
}
