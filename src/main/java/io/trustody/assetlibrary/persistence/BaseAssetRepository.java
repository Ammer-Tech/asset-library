package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.ledger.entities.assets.Asset;
import ammer.tech.commons.ledger.events.AssetChangeEvent;
import dev.morphia.query.experimental.filters.Filters;
import io.trustody.assetlibrary.incremental.EventQueueController;
import io.trustody.assetlibrary.models.SearchRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bouncycastle.asn1.DERSequence;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BaseAssetRepository implements AssetRepository<Asset> {

    @Inject
    private EventQueueController eventQueueController;


    @Override
    public List<Asset> searchRepository(SearchRequest searchRequest) {
        return datastore.find(Asset.class).filter(searchRequest.toFilter()).stream().collect(Collectors.toList());
    }

    @Override
    public List<Asset> listElements() {
        return null;
    }

    @Override
    public Asset upsertElement(Asset element) {
        //There is a corner case, because we are uploading files here - we need to find out what the hell.
        Asset a = datastore.find(Asset.class).filter(Filters.eq("id",element.getId())).first();
        if(a != null) {
            if (element.getGenericLogoUrl() == null && a.getGenericLogoUrl() != null)
                element.setGenericLogoUrl(a.getGenericLogoUrl());
            if (element.getIOSLogoUrl() == null && a.getIOSLogoUrl() != null)
                element.setGenericLogoUrl(a.getIOSLogoUrl());
            if (element.getAndroidLogoUrl() == null && a.getAndroidLogoUrl() != null)
                element.setGenericLogoUrl(a.getAndroidLogoUrl());
        }
        var x = datastore.save(element);
        try {
            eventQueueController.storeChangeEvent(AssetChangeEvent.builder()
                    .networkChange(false).codecType(element.getAssetType())
                    .objectId(element.getId()).deleted(false).changeData(new DERSequence(x.encodeToVector()).getEncoded()).build()
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return x;
    }

    @Override
    public boolean deleteElement(Asset element) {
        if(datastore.delete(element).getDeletedCount() == 1){
            eventQueueController.storeChangeEvent(AssetChangeEvent.builder()
                    .networkChange(false).codecType(element.getAssetType())
                    .objectId(element.getId()).deleted(true).changeData(null).build()
            );
            return true;
        }
        return false;
    }
}
