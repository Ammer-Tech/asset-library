package io.trustody.assetlibrary.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/api/public")
public class PublicApi {

    @GET
    @Path("/assets")
    public Response getAssets(){
        return Response.noContent().build();
    }

    @GET
    @Path("/changes")
    public Response getChanges(@QueryParam("startSequenceNumber") Long startSequenceNumber){
        return Response.noContent().build();
    }

}
