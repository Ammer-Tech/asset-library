package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.BaseAsset;
import dev.morphia.query.experimental.filters.Filters;

import java.util.List;
import java.util.UUID;

public class BaseAssetRepository implements AssetRepository<BaseAsset> {

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
        return datastore.save(element);
    }

    @Override
    public boolean deleteElement(BaseAsset element) {
        return datastore.delete(element).getDeletedCount() == 1;
    }
}
