package io.trustody.assetlibrary.api;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.accounts.PublicKeyType;
import com.jsoniter.output.JsonStream;
import io.trustody.assetlibrary.incremental.EventQueueController;
import io.trustody.assetlibrary.persistence.BaseAssetRepository;
import io.trustody.assetlibrary.persistence.MediaAssetRepository;
import io.trustody.assetlibrary.persistence.NetworkRepository;
import io.trustody.assetlibrary.persistence.SmartAssetRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.util.UUID;

@Path("/api/public")
@RequestScoped
@Slf4j
public class PublicApi {

    @Inject
    private BaseAssetRepository baseAssetRepository;
    @Inject
    private SmartAssetRepository smartAssetRepository;
    @Inject
    private MediaAssetRepository mediaAssetRepository;
    @Inject
    private NetworkRepository networkRepository;
    @Inject
    private EventQueueController eventQueueController;

    @GET
    @Path("/networks")
    @Timed(name = "inventoryProcessingTime",
            tags = {"method=get"},
            absolute = true,
            description = "Time needed to process the inventory")
    public Response getNetworks(){
        return Response.ok(JsonStream.serialize(networkRepository.listElements())).build() ;
    }

    @GET
    @Path("/networks/{networkId}/assets/native")
    public Response getAssets(@PathParam("networkId") String networkId){
        return Response.ok(JsonStream.serialize(baseAssetRepository.listElements(UUID.fromString(networkId)))).build() ;
    }

    @GET
    @Path("/networks/{networkId}/assets/smart-assets")
    public Response getSmartAssets(@PathParam("networkId") String networkId){
        return Response.ok(JsonStream.serialize(smartAssetRepository.listElements(UUID.fromString(networkId)))).build() ;
    }

    @GET
    @Path("/networks/{networkId}/assets/media-assets")
    public Response getMediaAssets(@PathParam("networkId") String networkId){
        return Response.ok(JsonStream.serialize(mediaAssetRepository.listElements(UUID.fromString(networkId)))).build() ;
    }

    @GET
    @Path("/changes")
    public Response getChanges(@QueryParam("startSequenceNumber") Long startSequenceNumber){
        System.out.println("Received request with start number " + startSequenceNumber);
        return Response.ok(JsonStream.serialize(eventQueueController.getEvents(startSequenceNumber))).build();
    }

    @GET
    @Path("/getSupportedCodecs")
    public Response getSupportedCodecs(){
        return Response.ok(JsonStream.serialize(CodecTypes.values())).build();
    }

    @GET
    @Path("/getSupportedPublicKeys")
    public Response getSupportedPublicKeys(){
        return Response.ok(JsonStream.serialize(PublicKeyType.values())).build();
    }

}
