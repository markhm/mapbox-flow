package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum ClassElement
{
    FERRY, MOTORWAY, TOLL, TUNNEL;

    @JsonValue
    public String toValue()
    {
        switch (this)
        {
            case FERRY:
                return "ferry";
            case MOTORWAY:
                return "motorway";
            case TOLL:
                return "toll";
            case TUNNEL:
                return "tunnel";
        }
        return null;
    }

    @JsonCreator
    public static ClassElement forValue(String value) throws IOException
    {
        if (value.equals("ferry")) return FERRY;
        if (value.equals("motorway")) return MOTORWAY;
        if (value.equals("toll")) return TOLL;
        if (value.equals("tunnel")) return TUNNEL;
        throw new IOException("Cannot deserialize ClassElement");
    }
}
