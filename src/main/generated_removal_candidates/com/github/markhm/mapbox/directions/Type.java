package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum Type
{
    LINE_STRING;

    @JsonValue
    public String toValue()
    {
        switch (this)
        {
            case LINE_STRING:
                return "LineString";
        }
        return null;
    }

    @JsonCreator
    public static Type forValue(String value) throws IOException
    {
        if (value.equals("LineString")) return LINE_STRING;
        throw new IOException("Cannot deserialize Type");
    }
}
