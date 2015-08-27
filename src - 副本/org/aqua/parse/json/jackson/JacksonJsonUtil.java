package org.aqua.parse.json.jackson;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonJsonUtil {
    private static JsonFactory factory;
    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        factory = mapper.getFactory();
    }
    public static <T> T getJava(String json, Class<T> T) {
        try {
            JsonParser parser = factory.createParser(json).enable(Feature.ALLOW_COMMENTS);
            return parser.readValueAs(T);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJson(Object source) {
        StringWriter writer = new StringWriter();
        try {
            factory.createGenerator(writer).writeObject(source);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
