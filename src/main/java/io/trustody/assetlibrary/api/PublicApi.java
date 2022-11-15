package io.trustody.assetlibrary.api;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.accounts.PublicKeyType;
import ammer.tech.commons.ledger.entities.assets.BaseAsset;
import ammer.tech.commons.ledger.entities.assets.MediaAsset;
import ammer.tech.commons.ledger.entities.assets.Network;
import ammer.tech.commons.ledger.entities.assets.SmartAsset;
import ammer.tech.commons.ledger.events.AssetChangeEvent;
import com.jsoniter.output.JsonStream;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.trustody.assetlibrary.incremental.EventQueueController;
import io.trustody.assetlibrary.persistence.BaseAssetRepository;
import io.trustody.assetlibrary.persistence.MediaAssetRepository;
import io.trustody.assetlibrary.persistence.NetworkRepository;
import io.trustody.assetlibrary.persistence.SmartAssetRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/api/public")
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
    @Operation(summary = "Get all networks",
            responses = {
                    @ApiResponse(description = "List of all networks", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Network.class))
                            )
                    ),
                    @ApiResponse(description = "Server-side processing error", responseCode = "500"),
            })
    public Response getNetworks(){
        return Response.ok(JsonStream.serialize(networkRepository.listElements())).build() ;
    }

    @GET
    @Path("/networks/{networkId}/assets/native")
    @Operation(summary = "Get all native coins",
            responses = {
                    @ApiResponse(description = "Get all native coins", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BaseAsset.class))
                            )
                    ),
                    @ApiResponse(description = "Server-side processing error", responseCode = "500"),
            })
    public Response getAssets(@PathParam("networkId") String networkId){
        return Response.ok(JsonStream.serialize(baseAssetRepository.listElements(UUID.fromString(networkId)))).build() ;
    }

    @GET
    @Path("/networks/{networkId}/assets/smart-assets")
    @Operation(summary = "Get all smart assets",
            responses = {
                    @ApiResponse(description = "List of all smart assets", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = SmartAsset.class))
                            )
                    ),
                    @ApiResponse(description = "Server-side processing error", responseCode = "500"),
            })
    public Response getSmartAssets(@PathParam("networkId") String networkId){
        return Response.ok(JsonStream.serialize(smartAssetRepository.listElements(UUID.fromString(networkId)))).build() ;
    }

    @GET
    @Path("/networks/{networkId}/assets/media-assets")
    @Operation(summary = "Get all media assets",
            responses = {
                    @ApiResponse(description = "List of all media assets", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = MediaAsset.class))
                            )
                    ),
                    @ApiResponse(description = "Server-side processing error", responseCode = "500"),
            })
    public Response getMediaAssets(@PathParam("networkId") String networkId){
        return Response.ok(JsonStream.serialize(mediaAssetRepository.listElements(UUID.fromString(networkId)))).build() ;
    }

    @GET
    @Path("/changes")
    @Operation(summary = "Get all changes starting from a sequence number",
            responses = {
                    @ApiResponse(description = "List of all changes", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AssetChangeEvent.class))
                            )
                    ),
                    @ApiResponse(description = "Server-side processing error", responseCode = "500"),
            })
    public Response getChanges(@QueryParam("startSequenceNumber") Long startSequenceNumber){
        System.out.println("Received request with start number " + startSequenceNumber);
        return Response.ok(JsonStream.serialize(eventQueueController.getEvents(startSequenceNumber))).build();
    }

    @GET
    @Path("/getSupportedCodecs")
    @Operation(summary = "Get all codecs which exist",
            responses = {
                    @ApiResponse(description = "List of all codecs", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CodecTypes.class))
                            )
                    ),
                    @ApiResponse(description = "Server-side processing error", responseCode = "500"),
            })
    public Response getSupportedCodecs(){
        return Response.ok(JsonStream.serialize(CodecTypes.values())).build();
    }

    @GET
    @Path("/getSupportedPublicKeys")
    @Operation(summary = "Get all codecs which exist",
            responses = {
                    @ApiResponse(description = "List of all codecs", responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CodecTypes.class))
                            )
                    ),
                    @ApiResponse(description = "Server-side processing error", PublicKeyType = "500"),
            })
    public Response getSupportedPublicKeys(){
        return Response.ok(JsonStream.serialize(PublicKeyType.values())).build();
    }

}
