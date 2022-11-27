package io.trustody.assetlibrary.incremental;

import ammer.tech.commons.ledger.entities.assets.Asset;
import ammer.tech.commons.ledger.entities.assets.Network;
import ammer.tech.commons.ledger.events.AssetChangeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class EventQueueController {

    @Inject
    private QueuePublisher publisher;

    public void storeChangeEvent(AssetChangeEvent changeEvent){
        changeEvent.setTopic("ASSET_CHANGE");
        publisher.getEventQueue().publish(changeEvent);
    }

    public List<Object> getEvents(long startSequence){
        long endSequence = publisher.getEventQueue().getSequenceNumber();
        log.info("End sequence is {}",endSequence);
        log.info("Start sequence is {}",startSequence);
        if(endSequence <= startSequence) return List.of();
        List<AssetChangeEvent> changeEvents = publisher.getEventQueue().getMessageBuffer().getElements(startSequence,endSequence);
        return changeEvents.stream().map(x -> {
            try {
                if (x.getNetworkChange()) return new Network((ASN1Sequence) DERSequence.fromByteArray(x.getChangeData()));
                else return new Asset((ASN1Sequence) DERSequence.fromByteArray(x.getChangeData()));
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }



}
