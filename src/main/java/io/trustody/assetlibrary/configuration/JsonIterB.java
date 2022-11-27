package io.trustody.assetlibrary.configuration;

import ammer.tech.commons.ledger.codec.UUIDCodec;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import com.jsoniter.spi.JsoniterSpi;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbException;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
public class JsonIterB implements Jsonb {

    private final HashMap<String,Class<?>> mappedClasses = new HashMap<>();

    public static void setup(){
        //Register encoders.
        JsoniterSpi.registerTypeEncoder(UUID.class,new UUIDCodec());
        //Register decoders.
        JsoniterSpi.registerTypeDecoder(UUID.class,new UUIDCodec());
        //Register key map encoders.
        JsoniterSpi.registerMapKeyEncoder(UUID.class,new UUIDCodec());
        JsoniterSpi.registerMapKeyDecoder(UUID.class,new UUIDCodec());
    }

    public JsonIterB(DecodingMode decodingMode, EncodingMode encodingMode) {
        //Set encode/decode modes (configurable).
        JsonIterator.setMode(decodingMode);
        JsonStream.setMode(encodingMode);
    }

    @Override
    public <T> T fromJson(String str, Class<T> type) throws JsonbException {
        return JsonIterator.deserialize(str,type);
    }

    @Override
    public <T> T fromJson(String str, Type runtimeType) throws JsonbException {
        try {
            mappedClasses.putIfAbsent(runtimeType.getTypeName(),Class.forName(runtimeType.getTypeName()));
            return (T) JsonIterator.deserialize(str,mappedClasses.get(runtimeType.getTypeName()));
        }
        catch (Exception e){
            throw new JsonbException("Could not deserialize from string and runtime type!");
        }
    }

    @Override
    public <T> T fromJson(Reader reader, Class<T> type) throws JsonbException {
        try {
            byte[] bytes = new byte[10_000];
            int i = 0;
            bytes[i] = (byte) reader.read();
            while (bytes[i] != -1) {
                bytes[i + 1] = (byte) reader.read();
                i = i + 1;
            }
            reader.close();
            return JsonIterator.deserialize(bytes, type);
        }
        catch (Exception e){
            throw new JsonbException("Could not deserialize from reader and type!");
        }
    }

    @Override
    public <T> T fromJson(Reader reader, Type runtimeType) throws JsonbException {
        try {
            byte[] bytes = new byte[10_000];
            int i = 0;
            bytes[i] = (byte) reader.read();
            while (bytes[i] != -1) {
                bytes[i + 1] = (byte) reader.read();
                i = i + 1;
            }
            reader.close();
            mappedClasses.putIfAbsent(runtimeType.getTypeName(),Class.forName(runtimeType.getTypeName()));
            return (T) JsonIterator.deserialize(bytes,mappedClasses.get(runtimeType.getTypeName()));
        }
        catch (Exception e){
            throw new JsonbException("Could not deserialize from reader and runtime type!");
        }
    }

    @Override
    public <T> T fromJson(InputStream stream, Class<T> type) throws JsonbException {
        try {
            return JsonIterator.deserialize(stream.readAllBytes(), type);
        }
        catch (Exception e){
            throw new JsonbException("Could not deserialize from input stream and type!");
        }
    }

    @Override
    public <T> T fromJson(InputStream stream, Type runtimeType) throws JsonbException {
        try {
            mappedClasses.putIfAbsent(runtimeType.getTypeName(),Class.forName(runtimeType.getTypeName()));
            return (T) JsonIterator.deserialize(stream.readAllBytes(),mappedClasses.get(runtimeType.getTypeName()));
        }
        catch (Exception e){
            throw new JsonbException("Could not deserialize from input stream and runtime type!");
        }
    }

    @Override
    public String toJson(Object object) throws JsonbException {
        return JsonStream.serialize(object);
    }

    @Override
    public String toJson(Object object, Type runtimeType) throws JsonbException {
        return JsonStream.serialize(object);
    }

    @Override
    public void toJson(Object object, Writer writer) throws JsonbException {
        try {
            byte[] bytes = JsonStream.serialize(object).getBytes();
            for (int i = 0; i < bytes.length; i++) {
                writer.write(bytes[i]);
            }
            writer.flush();
            writer.close();
        }
        catch (Exception e){
            throw new JsonbException("Could not write object!");
        }
    }

    @Override
    public void toJson(Object object, Type runtimeType, Writer writer) throws JsonbException {
        try {
            byte[] bytes = JsonStream.serialize(object).getBytes();
            for (int i = 0; i < bytes.length; i++) {
                writer.write(bytes[i]);
            }
            writer.flush();
            writer.close();
        }
        catch (Exception e){
            throw new JsonbException("Could not write object!");
        }
    }

    @Override
    public void toJson(Object object, OutputStream stream) throws JsonbException {
        try {
            stream.write(JsonStream.serialize(object).getBytes());
            stream.flush();
            stream.close();
        }
        catch (Exception e){
            throw new JsonbException("Could not write object to stream!");
        }
    }

    @Override
    public void toJson(Object object, Type runtimeType, OutputStream stream) throws JsonbException {
        try {
            stream.write(JsonStream.serialize(object).getBytes());
            stream.flush();
            stream.close();
        }
        catch (Exception e){
            throw new JsonbException("Could not write object to stream!");
        }
    }

    @Override
    public void close() throws Exception {

    }
}
