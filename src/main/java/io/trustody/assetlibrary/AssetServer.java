package io.trustody.assetlibrary;

import dev.morphia.Datastore;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@ApplicationPath("/assets")
@OpenAPIDefinition(info = @Info(
        title = "Asset Library for Crypto Projects",
        version = "1.0.0",
        contact = @Contact(
                name = "Ammer Group",
                email = "info@ammer.group",
                url = "https://ammer.group")),
        servers = {@Server(url = "/assets",description = "localhost")}

)
public class AssetServer extends Application {
    public static Datastore datastore;
}
