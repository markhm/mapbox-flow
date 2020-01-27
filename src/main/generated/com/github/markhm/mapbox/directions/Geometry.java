package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Geometry
{
    private List<List<Double>> coordinates;
    private Type type;

    @JsonProperty("coordinates")
    public List<List<Double>> getCoordinates()
    {
        return coordinates;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(List<List<Double>> value)
    {
        this.coordinates = value;
    }

    @JsonProperty("type")
    public Type getType()
    {
        return type;
    }

    @JsonProperty("type")
    public void setType(Type value)
    {
        this.type = value;
    }
}
