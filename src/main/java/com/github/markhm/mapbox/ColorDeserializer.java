package com.github.markhm.mapbox;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.markhm.mapbox.util.Color;

import java.io.IOException;

// https://www.baeldung.com/jackson-deserialization

public class ColorDeserializer extends StdDeserializer<Color>
{
    public ColorDeserializer()
    {
        this(null);
    }

    public ColorDeserializer(Class<?> vc)
    {
        super(vc);
    }

    @Override
    public Color deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
        JsonNode node = jp.getCodec().readTree(jp);

        String colorString = node.get("line-color").asText();

        // return new Color(colorString.substring(1, colorString.length() - 1));
        return new Color(colorString);
    }
}

