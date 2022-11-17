package io.trustody.assetlibrary.api;

import ammer.tech.commons.ledger.entities.assets.BaseAsset;
import ammer.tech.commons.ledger.entities.assets.MediaAsset;
import ammer.tech.commons.ledger.entities.assets.Network;
import ammer.tech.commons.ledger.entities.assets.SmartAsset;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import io.trustody.assetlibrary.persistence.BaseAssetRepository;
import io.trustody.assetlibrary.persistence.MediaAssetRepository;
import io.trustody.assetlibrary.persistence.NetworkRepository;
import io.trustody.assetlibrary.persistence.SmartAssetRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBodySchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@RequestScoped
@Path("/api/admin")
@OpenAPIDefinition(info = @Info(title="Greeting API", version = "1.0.0"))
@Tag(name = "Config Retrieval service", description = "Get the value for a certain config")
public class AdminApi {

    @Inject
    private NetworkRepository networkRepository;
    @Inject
    private SmartAssetRepository smartAssetRepository;
    @Inject
    private MediaAssetRepository mediaAssetRepository;
    @Inject
    private BaseAssetRepository baseAssetRepository;

    @PUT
    @Path("/network")
    @Operation(summary = "Finds Pets by status", description = "Multiple status values can be provided with comma separated strings")
    @RequestBodySchema(Network.class)
    @APIResponse(description = "Successful creation of a request for signature", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Network.class)))
    public Response appendNetwork(@RequestBody String body) throws Exception {
        Network network = JsonIterator.deserialize(body, Network.class);
        return Response.ok(JsonStream.serialize(networkRepository.upsertElement(network))).build();
    }

    @DELETE
    @Path("/networks/{networkId}")
    public Response deleteNetwork(@PathParam("networkId") String networkId) {
        if (networkRepository.deleteElement(Network.builder().id(UUID.fromString(networkId)).build()))
            return Response.ok().build();
        else return Response.serverError().build();
    }

    @PUT
    @Path("/asset")
    public Response appendAsset(@Context HttpServletRequest request) throws Exception {
        byte[] objectBytes = request.getInputStream().readAllBytes();
        BaseAsset baseAsset = JsonIterator.deserialize(objectBytes, BaseAsset.class);
        switch (baseAsset.getAssetType()) {
            case NATIVE:
                baseAssetRepository.upsertElement(baseAsset);
                break;
            case ERC20:
                SmartAsset asset0 = JsonIterator.deserialize(objectBytes,SmartAsset.class);
                smartAssetRepository.upsertElement(asset0);
                break;
            case ERC721:
            case ERC1155:
                MediaAsset asset1 = JsonIterator.deserialize(objectBytes,MediaAsset.class);
                mediaAssetRepository.upsertElement(asset1);
                break;
            default:
                break;
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/assets/{assetId}")
    public Response deleteAsset(@PathParam("assetId") String assetId) {
        if (smartAssetRepository.deleteElement(SmartAsset.builder().id(UUID.fromString(assetId)).build()))
            return Response.ok().build();
        else return Response.serverError().build();
    }
}
