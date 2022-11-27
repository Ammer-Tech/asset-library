package io.trustody.assetlibrary.api;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.Asset;
import ammer.tech.commons.ledger.entities.assets.Network;
import io.trustody.assetlibrary.persistence.BaseAssetRepository;
import io.trustody.assetlibrary.persistence.NetworkRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.io.File;
import java.math.BigInteger;
import java.util.UUID;

@RequestScoped
@Path("/api/admin")
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Greeting API", version = "1.0.0"))
@Tag(name = "Config Retrieval service", description = "Get the value for a certain config")
public class AdminApi {

    @Inject
    private NetworkRepository networkRepository;
    @Inject
    private BaseAssetRepository baseAssetRepository;

    @POST
    @Path("/network")
    @Operation(summary = "Create/Modify a Network")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(description = "The network that has been modified/created",
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Network.class))
    )
    public Response appendNetwork(@RequestBody Network network) throws Exception {
        return Response.ok(networkRepository.upsertElement(network)).build();
    }

    @DELETE
    @Path("/networks/{networkId}")
    public Response deleteNetwork(@PathParam("networkId") String networkId) {
        if (networkRepository.deleteElement(Network.builder().id(UUID.fromString(networkId)).build()))
            return Response.ok().build();
        else return Response.serverError().build();
    }

    @POST
    @Path("/asset")
    @Operation(summary = "Create/Modify an Asset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(description = "The asset that has been modified/created",
            responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Asset.class))
    )
    public Response appendAsset(@Multipart("parent") String parent,
                                @Multipart(value = "id",required = false) String id,
                                @Multipart("displayFactor") String displayFactor,
                                @Multipart("decimalSeparator") String decimalSeparator,
                                @Multipart("symbol") String symbol,
                                @Multipart("assetType") CodecTypes assetType,
                                @Multipart("visibleName") String visibleName,
                                @Multipart("visible") Boolean visible,
                                @Multipart(value = "description", required = false) String description,
                                @Multipart("feeEnabled") Boolean feeEnabled,
                                @Multipart("feeUnits") String feeUnits,
                                @Multipart(value = "genericLogo", required = false) Attachment genericLogo,
                                @Multipart(value = "androidLogo",required = false) Attachment androidLogo,
                                @Multipart(value = "iOSLogo",required = false) Attachment iOSLogo,
                                @Multipart(value = "address",required = false) String address,
                                @Multipart(value = "tokenId",required = false) String tokenId,
                                @Multipart(value = "mediaUrl",required = false) String mediaUrl,
                                //This is used if we are minting something - this is tricky, do not use this for now.
                                @Multipart(value = "mediaData",required = false) Attachment mediaData,
                                @Multipart(value = "mediaType",required = false) String mediaType,
                                @Multipart(value = "mediaDescription",required = false) String mediaDescription
    ) throws Exception {
        UUID uuid;
        if(id == null) uuid = UUID.randomUUID();
        else uuid = UUID.fromString(id);

        Asset.AssetBuilder builder = Asset.builder().parent(UUID.fromString(parent)).displayFactor(new BigInteger(displayFactor))
                .decimalSeparator(new BigInteger(decimalSeparator))
                .symbol(symbol).assetType(assetType).visibleName(visibleName).visible(visible)
                .description(description).feeEnabled(feeEnabled).feeUnits(new BigInteger(feeUnits))
                .address(address).tokenId(tokenId).mediaUrl(mediaUrl).mediaType(mediaType).mediaDescription(mediaDescription);

        if(genericLogo != null) {
            String genericLogoFilename = uuid + "-generic." + genericLogo.getHeaders().get("Content-Type").get(0).split("/")[1].toLowerCase();
            genericLogo.transferTo(new File("." + genericLogoFilename));
            builder.genericLogoUrl(genericLogoFilename);
        }

        if(androidLogo != null) {
            String androidLogoFilename = uuid + "-android." + androidLogo.getHeaders().get("Content-Type").get(0).split("/")[1].toLowerCase();
            androidLogo.transferTo(new File("." + androidLogoFilename));
            builder.genericLogoUrl(androidLogoFilename);
        }

        if(iOSLogo != null) {
            String iOSLogoFilename = uuid + "-ios." + iOSLogo.getHeaders().get("Content-Type").get(0).split("/")[1].toLowerCase();
            iOSLogo.transferTo(new File("." + iOSLogoFilename));
            builder.genericLogoUrl(iOSLogoFilename);
        }

        //Smash media url if it is internal and we did this via uploading.
        if(mediaData != null){
            String mediaDataFilename = uuid + "-media." + mediaData.getHeaders().get("Content-Type").get(0).split("/")[1].toLowerCase();
            mediaData.transferTo(new File("." + mediaDataFilename));
            builder.genericLogoUrl(mediaDataFilename);
            builder.mediaUrl(mediaDataFilename);
        }

        baseAssetRepository.upsertElement(builder.build());
        return Response.ok().build();
    }

    @DELETE
    @Path("/assets/{assetId}")
    public Response deleteAsset(@PathParam("assetId") String assetId) {
        if (baseAssetRepository.deleteElement(Asset.builder().id(UUID.fromString(assetId)).build()))
            return Response.ok().build();
        else return Response.serverError().build();
    }
}
