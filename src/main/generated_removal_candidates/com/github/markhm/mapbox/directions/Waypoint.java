package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Waypoint
{
    private double distance;
    private String name;
    private List<Double> location;

    @JsonProperty("distance")
    public double getDistance()
    {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(double value)
    {
        this.distance = value;
    }

    @JsonProperty("name")
    public String getName()
    {
        return name;
    }

    @JsonProperty("name")
    public void setName(String value)
    {
        this.name = value;
    }

    @JsonProperty("location")
    public List<Double> getLocation()
    {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(List<Double> value)
    {
        this.location = value;
    }

    public String toString()
    {
        return "[" + location.get(0) + ", " + location.get(1) + "]";
    }
}
