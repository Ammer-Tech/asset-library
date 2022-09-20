package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.ledger.entities.assets.MediaAsset;
import dev.morphia.query.experimental.filters.Filters;

import java.util.List;
import java.util.UUID;

public class MediaAssetRepository implements AssetRepository<MediaAsset> {

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
        return datastore.save(element);
    }

    @Override
    public boolean deleteElement(MediaAsset element) {
        return datastore.delete(element).getDeletedCount() == 1;
    }
}
