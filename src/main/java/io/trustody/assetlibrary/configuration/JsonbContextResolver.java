package io.trustody.assetlibrary.configuration;

import com.jsoniter.output.EncodingMode;
import com.jsoniter.spi.DecodingMode;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class JsonbContextResolver implements ContextResolver<Jsonb> {

    private final Jsonb jsonb;

    public JsonbContextResolver() {
        jsonb = new JsonIterB(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH, EncodingMode.DYNAMIC_MODE);
    }

    @Override
    public Jsonb getContext(Class<?> type) {
        return jsonb;
    }
}
