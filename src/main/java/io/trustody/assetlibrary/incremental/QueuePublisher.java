package io.trustody.assetlibrary.incremental;

import ammer.tech.commons.configuration.models.StreamConfig;
import ammer.tech.commons.ledger.events.AssetChangeEvent;
import ammer.tech.commons.utils.generic.SocketUtils;
import com.blockfeed.messaging.core.api.Protocol;
import com.blockfeed.messaging.core.extra.simple.DBMessageStorageContainer;
import com.blockfeed.messaging.core.streaming.publisher.MessageBuffer;
import com.blockfeed.messaging.core.streaming.publisher.Publisher;
import com.jsoniter.output.JsonStream;
import io.trustody.assetlibrary.configuration.Configuration;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.List;

@ApplicationScoped
public class QueuePublisher {

    @Inject
    private Configuration configuration;
    @Getter
    private Publisher<AssetChangeEvent> eventQueue;

    @PostConstruct
    public void init() {
        StreamConfig config;
        boolean bindCompleted = false;
        while (!bindCompleted) {
            try {
                int basePort = SocketUtils.nextFreePort(28000, 28100);
                config = new StreamConfig();
                config.setTopic("ASSET_CHANGE");
                config.setBindPub("tcp://*:" + basePort);
                basePort = SocketUtils.nextFreePort(28000, 28100);
                config.setBindRec("tcp://*:" + basePort);
                config.setValidTopics(List.of("ASSET_CHANGE"));
                MessageBuffer<AssetChangeEvent> txBuffer = new MessageBuffer<>(1, new DBMessageStorageContainer<>(AssetChangeEvent.class, Protocol.ASN1));
                eventQueue = new Publisher<>(config.getBindPub(), config.getBindRec(), txBuffer, Protocol.ASN1);
                configuration.getMetasToPublish().put(config.getTopic(), JsonStream.serialize(config));
                bindCompleted = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
