package io.trustody.assetlibrary.persistence;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import ammer.tech.commons.ledger.entities.assets.BaseAsset;
import ammer.tech.commons.ledger.entities.assets.MediaAsset;
import ammer.tech.commons.ledger.entities.assets.SmartAsset;
import ammer.tech.commons.persistence.mongodb.codecs.BigIntegerCodec;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
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
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static io.trustody.assetlibrary.AssetServer.datastore;
import static java.util.UUID.randomUUID;

@Slf4j
class RepositoryRepositoryTest {

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
        UUID parentId = randomUUID();
        ammer.tech.commons.ledger.entities.assets.Network network = new ammer.tech.commons.ledger.entities.assets.Network();
        network.setNetworkId(1293L);
        network.setId(parentId);
        network.setNetworkName("Ethereum Random Network");
        NetworkRepository networkRepository = new NetworkRepository();
        networkRepository.upsertElement(network);
        List<ammer.tech.commons.ledger.entities.assets.Network> networkList = networkRepository.listElements();
        Assertions.assertEquals(1,networkList.size());
        BaseAssetRepository baseAssetRepository = new BaseAssetRepository();
        baseAssetRepository.upsertElement( BaseAsset.builder().assetType(CodecTypes.NATIVE).id(randomUUID())
                .feeUnits(BigInteger.ONE).parent(parentId).build());
        var l1 = baseAssetRepository.listElements(parentId);
        Assertions.assertEquals(1,l1.size());
        //Check the case when we want to get a smart asset and a media asset.
        SmartAsset smartAsset = SmartAsset.builder().assetType(CodecTypes.ERC20).parent(parentId).id(randomUUID())
                .feeUnits(BigInteger.ONE).build();
        UUID mediaId = UUID.randomUUID();
        MediaAsset mediaAsset = MediaAsset.builder().assetType(CodecTypes.ERC721).parent(parentId).id(mediaId)
                .feeUnits(BigInteger.TEN).build();
        SmartAssetRepository smartAssetRepository = new SmartAssetRepository();
        MediaAssetRepository mediaAssetRepository = new MediaAssetRepository();
        smartAssetRepository.upsertElement(smartAsset);
        mediaAssetRepository.upsertElement(mediaAsset);
        List<SmartAsset> l2 = smartAssetRepository.listElements(parentId);
        Assertions.assertEquals(2,l2.size());
        //Now try to delete an asset based on the uuid.
        SmartAsset smartAsset1 = new SmartAsset();
        smartAsset1.setId(mediaId);
        smartAssetRepository.deleteElement(smartAsset1);
        List<SmartAsset> l3 = smartAssetRepository.listElements(parentId);
        Assertions.assertEquals(1,l3.size());
    }

    @AfterAll
    public static void destory(){
        mongodExecutable.stop();
    }

}