package io.trustody.assetlibrary.startup;

import ammer.tech.commons.persistence.mongodb.codecs.BigIntegerCodec;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.internal.MongoClientImpl;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import io.trustody.assetlibrary.configuration.Configuration;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import static io.trustody.assetlibrary.AssetServer.datastore;

@Slf4j
@WebListener
public class AssetServerInitializer implements ServletContextListener {

    @Inject
    private Configuration configuration;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                com.mongodb.MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(new BigIntegerCodec())
        );
        log.info("Mongo Registry Loaded...");
        MongoClientImpl mc;
        /*
        try {
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                    .codecRegistry(codecRegistry)
                    .applyConnectionString(new ConnectionString("mongodb://" +
                            configuration.getMongoDBConfiguration().getUsername() + ":" + configuration.getMongoDBConfiguration().getPassword()
                            + "@" + configuration.getMongoDBConfiguration().getServerAddress() + ":" + configuration.getMongoDBConfiguration().getPort()
                            + "/" + configuration.getMongoDBConfiguration().getDbName()))
                    .build();
            MongoDriverInformation mongoDriverInformation = MongoDriverInformation.builder().build();
            mc  = new MongoClientImpl(mongoClientSettings, mongoDriverInformation);
            log.info("Could not open a secure channel!");
        }
        catch (Exception e){

         */
            log.info("Could not open secure channel - opening plain.");
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                    .codecRegistry(codecRegistry)
                    .applyConnectionString(new ConnectionString("mongodb://" + configuration.getMongoDBConfiguration().getServerAddress() + ":" + configuration.getMongoDBConfiguration().getPort()
                            + "/" + configuration.getMongoDBConfiguration().getDbName()))
                    .build();
            MongoDriverInformation mongoDriverInformation = MongoDriverInformation.builder().build();
            mc = new MongoClientImpl(mongoClientSettings, mongoDriverInformation);

        datastore = Morphia.createDatastore(mc, "assets", MapperOptions.builder().build());
        datastore.getMapper().mapPackage("ammer.tech.commons.ledger.entities.assets");
        datastore.ensureIndexes();
        log.info("Asset server started succesfully!");
    }
}
