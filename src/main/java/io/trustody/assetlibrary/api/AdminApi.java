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
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.UUID;

@Path("/api/admin")
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
