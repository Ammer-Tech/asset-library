package io.trustody.assetlibrary.startup;

import ammer.tech.commons.persistence.mongodb.codecs.BigIntegerCodec;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.internal.MongoClientImpl;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import static io.trustody.assetlibrary.AssetServer.datastore;

public class AssetServerInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                com.mongodb.MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(new BigIntegerCodec())
        );
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .codecRegistry(codecRegistry)
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017"))
                .build();
        MongoDriverInformation mongoDriverInformation = MongoDriverInformation.builder().build();
        var mc = new MongoClientImpl(mongoClientSettings,mongoDriverInformation);
        datastore = Morphia.createDatastore(mc, "assets", MapperOptions.builder().build());
        datastore.getMapper().mapPackage("ammer.tech.commons.ledger.entities.assets");
        datastore.ensureIndexes();
    }
}
