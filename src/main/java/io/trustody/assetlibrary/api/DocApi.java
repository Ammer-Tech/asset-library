package io.trustody.assetlibrary.api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/openapi")
@RequestScoped
public class DocApi {

    //Basically this returns the YAML file.
    @GET
    public Response getApiDocs() throws Exception{
        return Response.ok(new String(
                this.getClass().getClassLoader().getResourceAsStream("openapi/openapi.yaml").readAllBytes())
        ).build();
    }
}
