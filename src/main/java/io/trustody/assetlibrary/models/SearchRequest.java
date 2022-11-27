package io.trustody.assetlibrary.models;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import dev.morphia.query.experimental.filters.Filter;
import dev.morphia.query.experimental.filters.Filters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class SearchRequest {
    private UUID networkId;
    private CodecTypes codecType;
    private String name;
    private String contractAddress;

    public Filter toFilter(){
        List<Filter> filterList = new ArrayList<>(1);
        if(networkId != null) filterList.add(Filters.eq("parent",networkId));
        if(codecType != null) filterList.add(Filters.eq("assetType",codecType));
        if(name != null) filterList.add(Filters.text(name));
        if(contractAddress != null) filterList.add(Filters.eq("address",contractAddress));
        return Filters.and(filterList.toArray(Filter[]::new));
    }

}
