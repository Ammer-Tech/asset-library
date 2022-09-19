package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.BaseAsset;
import ammer.tech.commons.persistence.mongodb.codecs.BigIntegerCodec;
import com.jsoniter.output.JsonStream;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.UuidRepresentation;
import org.bson.codecs.BigDecimalCodec;
import org.bson.codecs.UuidCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.UUID;

import static io.trustody.assetlibrary.AssetServer.datastore;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BaseAssetRepositoryTest {

    private static MongodExecutable mongodExecutable;

    @BeforeAll
    public static void init() throws Exception{
        String ip = "localhost";
        int port = 27017;

        ImmutableMongodConfig mongodConfig = MongodConfig
                .builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(com.mongodb.MongoClient.getDefaultCodecRegistry(),CodecRegistries.fromCodecs(new BigIntegerCodec()));
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


    @Test
    public void testRepo(){
        UUID parentId = UUID.randomUUID();
        BaseAssetRepository baseAssetRepository = new BaseAssetRepository();
        baseAssetRepository.upsertElement( BaseAsset.builder().assetType(CodecTypes.NATIVE).id(UUID.randomUUID())
                .feeUnits(BigInteger.ONE).parent(parentId).build());
        var l = baseAssetRepository.listElements(parentId);
        log.info(JsonStream.serialize(l));
    }

    @AfterAll
    public static void destory(){
        mongodExecutable.stop();
    }

}