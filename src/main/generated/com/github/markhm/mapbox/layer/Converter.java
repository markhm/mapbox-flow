package com.github.markhm.mapbox.layer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.markhm.mapbox.util.Color;
import com.github.markhm.mapbox.util.ColorDeserializer;
import com.github.markhm.mapbox.util.ColorSerializer;

import java.io.IOException;

public class Converter
{
    // Serialize/deserialize helpers

    public static Layer fromJsonString(String json) throws IOException
    {
        return getObjectReader().readValue(json);
    }

    public static String toJsonString(Layer obj) throws JsonProcessingException
    {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper()
    {
        ObjectMapper mapper = new ObjectMapper();

        // Added custom deserializer
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        module.addSerializer(Color.class, new ColorSerializer());
        mapper.registerModule(module);

        reader = mapper.readerFor(Layer.class);
        writer = mapper.writerFor(Layer.class);
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

