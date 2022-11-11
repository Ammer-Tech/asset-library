package io.trustody.assetlibrary.incremental;

import ammer.tech.commons.ledger.events.AssetChangeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class EventQueueController {

    @Inject
    private QueuePublisher publisher;

    public synchronized void storeChangeEvent(AssetChangeEvent changeEvent){
        publisher.getEventQueue().publish(changeEvent);
    }

    public List<AssetChangeEvent> getEvents(long startSequence){
        return null;
    }



}
