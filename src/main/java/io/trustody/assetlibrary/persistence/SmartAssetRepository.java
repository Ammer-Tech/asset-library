package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.SmartAsset;
import dev.morphia.query.experimental.filters.Filters;

import java.util.List;
import java.util.UUID;

public class SmartAssetRepository implements AssetRepository<SmartAsset> {

    @Override
    public List<SmartAsset> listElements(UUID id) {
        var f = Filters.and(Filters.eq("parent",id),Filters.eq("assetType", CodecTypes.ERC20));
        return datastore.find(SmartAsset.class).filter(f).iterator().toList();
    }

    @Override
    public SmartAsset upsertElement(SmartAsset element) {
        return datastore.save(element);
    }

    @Override
    public boolean deleteElement(SmartAsset element) {
        return datastore.delete(element).getDeletedCount() == 1;
    }
}
