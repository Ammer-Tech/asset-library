package io.trustody.assetlibrary.incremental;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChangeEvent {
    private Long sequenceNumber;
    private Boolean networkChange;
    private CodecTypes codecType;
    private UUID objectId;
    private Boolean deleted;
    private String changeData;
}
