package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum Mode
{
    DRIVING, FERRY;

    @JsonValue
    public String toValue()
    {
        switch (this)
        {
            case DRIVING:
                return "driving";
            case FERRY:
                return "ferry";
        }
        return null;
    }

    @JsonCreator
    public static Mode forValue(String value) throws IOException
    {
        if (value.equals("driving")) return DRIVING;
        if (value.equals("ferry")) return FERRY;
        throw new IOException("Cannot deserialize Mode");
    }
}
