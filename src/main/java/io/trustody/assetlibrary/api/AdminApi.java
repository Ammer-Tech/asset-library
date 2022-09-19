package io.trustody.assetlibrary.api;

import io.trustody.assetlibrary.persistence.AssetRepository;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("/api/admin")
public class AdminApi {

    @Inject
    private AssetRepository assetRepository;

    @POST
    public Response createOrModifyAsset(@Context HttpServletRequest request){
       return null;
    }

    @DELETE
    public Response addAsset(){
        return Response.noContent().build();
    }

}
