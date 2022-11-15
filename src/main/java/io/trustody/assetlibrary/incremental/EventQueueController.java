package io.trustody.assetlibrary.incremental;

import ammer.tech.commons.ledger.events.AssetChangeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class EventQueueController {

    @Inject
    private QueuePublisher publisher;

    public void storeChangeEvent(AssetChangeEvent changeEvent){
        changeEvent.setTopic("ASSET_CHANGE");
        publisher.getEventQueue().publish(changeEvent);
    }

    public List<AssetChangeEvent> getEvents(long startSequence){
        long endSequence = publisher.getEventQueue().getSequenceNumber();
        log.info("End sequence is {}",endSequence);
        log.info("Start sequence is {}",startSequence);
        if(endSequence <= startSequence) return List.of();
        return publisher.getEventQueue().getMessageBuffer().getElements(startSequence,endSequence);
    }



}
