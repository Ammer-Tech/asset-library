package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.MediaAsset;
import dev.morphia.query.experimental.filters.Filters;

import java.util.List;
import java.util.UUID;

public class MediaAssetRepository implements AssetRepository<MediaAsset> {

    @Override
    public List<MediaAsset> listElements(UUID id) {
        var f = Filters.and(Filters.eq("parent", id),
                Filters.or(Filters.eq("codecType", CodecTypes.ERC721), Filters.eq("assetType", CodecTypes.ERC1155))
        );
        return datastore.find(MediaAsset.class).filter(f).iterator().toList();
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
