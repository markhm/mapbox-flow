package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum DrivingSide
{
    LEFT, RIGHT, SLIGHT_LEFT, SLIGHT_RIGHT, STRAIGHT;

    @JsonValue
    public String toValue()
    {
        switch (this)
        {
            case LEFT:
                return "left";
            case RIGHT:
                return "right";
            case SLIGHT_LEFT:
                return "slight left";
            case SLIGHT_RIGHT:
                return "slight right";
            case STRAIGHT:
                return "straight";
        }
        return null;
    }

    @JsonCreator
    public static DrivingSide forValue(String value) throws IOException
    {
        if (value.equals("left")) return LEFT;
        if (value.equals("right")) return RIGHT;
        if (value.equals("slight left")) return SLIGHT_LEFT;
        if (value.equals("slight right")) return SLIGHT_RIGHT;
        if (value.equals("straight")) return STRAIGHT;
        throw new IOException("Cannot deserialize DrivingSide");
    }
}
