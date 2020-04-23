package com.github.markhm.mapbox;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.markhm.mapbox.util.Color;

import java.io.IOException;

// https://www.baeldung.com/jackson-custom-serialization

public class ColorSerializer extends StdSerializer<Color>
{
    public ColorSerializer()
    {
        this(null);
    }

    public ColorSerializer(Class<Color> vc)
    {
        super(vc);
    }

    @Override
    public void serialize(Color value, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        // gen.writeStartObject();
        gen.writeString(value.toString());
        // gen.writeStringField("line-color", value.toString());
        // gen.writeEndObject();
    }
}
