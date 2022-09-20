package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.SmartAsset;
import dev.morphia.query.experimental.filters.Filters;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SmartAssetRepository implements AssetRepository<SmartAsset> {

    @Override
    public List<SmartAsset> listElements(UUID id) {
        var f = Filters.and(
                Filters.eq("parent", id),
                Filters.ne("assetType", CodecTypes.NATIVE)
        );

        return datastore.find("assets",Object.class).disableValidation()
                .filter(f).iterator().toList().stream()
                .map(o -> (SmartAsset) o).collect(Collectors.toList());
    }

    @Override
    public List<SmartAsset> listElements() {
        return null;
    }

    @Override
    public SmartAsset upsertElement(SmartAsset element) {
        if(element.getId() == null) element.setId(UUID.randomUUID());
        return datastore.save(element);
    }

    @Override
    public boolean deleteElement(SmartAsset element) {
        return datastore.delete(element).getDeletedCount() == 1;
    }
}
