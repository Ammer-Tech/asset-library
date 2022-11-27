package io.trustody.assetlibrary.api;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.accounts.PublicKeyType;
import ammer.tech.commons.ledger.entities.assets.Asset;
import ammer.tech.commons.ledger.entities.assets.Network;
import io.trustody.assetlibrary.incremental.EventQueueController;
import io.trustody.assetlibrary.models.SearchRequest;
import io.trustody.assetlibrary.persistence.BaseAssetRepository;
import io.trustody.assetlibrary.persistence.NetworkRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/api/public")
@RequestScoped
@Slf4j
public class PublicApi {

    @Inject
    private BaseAssetRepository baseAssetRepository;
    @Inject
    private NetworkRepository networkRepository;
    @Inject
    private EventQueueController eventQueueController;

    @GET
    @Path("/networks")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(description = "All the supported networks",
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Network[].class))
    )
    public Response getNetworks(){
        return Response.ok(networkRepository.listElements()).build() ;
    }

    @POST
    @APIResponse(description = "All assets given a filter",
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Asset[].class))
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssets(@RequestBody SearchRequest searchRequest){
        return Response.ok(baseAssetRepository.searchRepository(searchRequest)).build();
    }

    @GET
    @Path("/changes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChanges(@QueryParam("startSequenceNumber") Long startSequenceNumber){
        System.out.println("Received request with start number " + startSequenceNumber);
        return Response.ok(eventQueueController.getEvents(startSequenceNumber)).build();
    }

    @GET
    @Path("/getSupportedCodecs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupportedCodecs(){
        return Response.ok(CodecTypes.values()).build();
    }

    @GET
    @Path("/getSupportedPublicKeys")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupportedPublicKeys(){
        return Response.ok(PublicKeyType.values()).build();
    }

}
