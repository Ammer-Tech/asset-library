package io.trustody.assetlibrary.models;

import ammer.tech.commons.blockchain.l2codecs.CodecTypes;
import com.jsoniter.annotation.JsonIgnore;
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
    private UUID parent;
    private CodecTypes codecType;
    private String visibleName;
    private String symbol;
    private String contractAddress;

    @JsonIgnore
    public Filter toFilter(){
        List<Filter> filterList = new ArrayList<>(1);
        if(parent != null) filterList.add(Filters.eq("parent",parent));
        if(codecType != null) filterList.add(Filters.eq("assetType",codecType));
        if(visibleName != null) filterList.add(Filters.regex("visibleName").pattern("^.*" + visibleName + ".*$"));
        if(contractAddress != null) filterList.add(Filters.eq("address",contractAddress));
        if(symbol != null) filterList.add(Filters.regex("symbol").pattern("^.*" + symbol + ".*$"));
        return Filters.and(filterList.toArray(Filter[]::new));
    }

}
