// To use this code, add the following Maven dependency to your project:
//
//     com.fasterxml.jackson.core : jackson-databind : 2.9.0
//
// Import this package:
//
//     import com.github.markhm.mapbox.directions.Converter;
//
// Then you can deserialize a JSON string with
//
//     Welcome data = Converter.fromJsonString(jsonString);

package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class Converter
{
    // Serialize/deserialize helpers

    public static DirectionsResponse fromJsonString(String json) throws IOException
    {
        return getObjectReader().readValue(json);
    }

    public static String toJsonString(DirectionsResponse obj) throws JsonProcessingException
    {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(DirectionsResponse.class);
        writer = mapper.writerFor(DirectionsResponse.class);
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
