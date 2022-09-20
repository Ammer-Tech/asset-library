package io.trustody.assetlibrary.api;

import ammer.tech.commons.ledger.entities.assets.BaseAsset;
import ammer.tech.commons.ledger.entities.assets.Network;
import ammer.tech.commons.ledger.entities.assets.SmartAsset;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
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

import java.util.UUID;

@Path("/api/admin")
public class AdminApi {

    @Inject
    private NetworkRepository networkRepository;
    @Inject
    private SmartAssetRepository smartAssetRepository;

    @PUT
    @Path("/networks/add")
    public Response appendNetwork(@Context HttpServletRequest request) throws Exception {
        Network network = JsonIterator.deserialize(request.getInputStream().readAllBytes(), Network.class);
        return Response.ok(JsonStream.serialize(networkRepository.upsertElement(network))).build();
    }

    @DELETE
    @Path("/networks/delete/{networkId}")
    public Response deleteNetwork(@PathParam("networkId") String networkId) {
        if (networkRepository.deleteElement(Network.builder().id(UUID.fromString(networkId)).build()))
            return Response.ok().build();
        else return Response.serverError().build();
    }

    @PUT
    @Path("/assets/add")
    public Response appendAsset(@Context HttpServletRequest request) throws Exception {
        byte[] objectBytes = request.getInputStream().readAllBytes();
        SmartAsset smartAsset = JsonIterator.deserialize(objectBytes, SmartAsset.class);
        switch (smartAsset.getAssetType()) {
            case ERC20:
                break;
            case ERC721:
                break;
            case ERC1155:
                break;
        }
        return Response.notModified().build();
    }

    @DELETE
    @Path("/assets/delete/{assetId}")
    public Response deleteAsset(@PathParam("assetId") String assetId) {
        if (smartAssetRepository.deleteElement(SmartAsset.builder().id(UUID.fromString(assetId)).build()))
            return Response.ok().build();
        else return Response.serverError().build();
    }
}
