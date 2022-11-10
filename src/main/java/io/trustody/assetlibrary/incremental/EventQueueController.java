package io.trustody.assetlibrary.incremental;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EventQueueController {

    public synchronized void storeChangeEvent(ChangeEvent changeEvent){

    }

    public List<ChangeEvent> getEvents(long startSequence){
        return null;
    }



}
