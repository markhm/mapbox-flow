package com.github.markhm.mapbox.layer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class SourceConverter
{
    // Serialize/deserialize helpers

    public static Source fromJsonString(String json) throws IOException
    {
        return getObjectReader().readValue(json);
    }

    public static String toJsonString(Source obj) throws JsonProcessingException
    {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper()
    {
        ObjectMapper mapper = new ObjectMapper();

//        // Added custom deserializer
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(Color.class, new ColorDeserializer());
//        module.addSerializer(Color.class, new ColorSerializer());
//        mapper.registerModule(module);

        reader = mapper.readerFor(Source.class);
        writer = mapper.writerFor(Source.class);
    }

    private static ObjectReader getObjectReader()
    {
        if (reader == null) instantiateMapper();
        return reader;
    }

    private static ObjectWriter getObjectWriter()
    {
        if (writer == null) instantiateMapper();
        return writer;
    }
}
