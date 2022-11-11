package io.trustody.assetlibrary.api;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import com.jsoniter.output.JsonStream;
import io.trustody.assetlibrary.incremental.EventQueueController;
import io.trustody.assetlibrary.persistence.BaseAssetRepository;
import io.trustody.assetlibrary.persistence.MediaAssetRepository;
import io.trustody.assetlibrary.persistence.NetworkRepository;
import io.trustody.assetlibrary.persistence.SmartAssetRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import javax.inject.Inject;
import java.util.UUID;

@Path("/api/public")
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
        return Response.ok(JsonStream.serialize(eventQueueController.getEvents(startSequenceNumber))).build();
    }

    @GET
    @Path("/getSupportedCodecs")
    public Response getSupportedCodecs(){
        return Response.ok(JsonStream.serialize(CodecTypes.values())).build();
    }
}
