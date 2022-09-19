package io.trustody.assetlibrary;

import dev.morphia.Datastore;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/assets")
public class AssetServer extends Application {
    public static Datastore datastore;
}
