package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.markhm.mapbox.style.Geometry;

import java.util.List;

public class Route
{
    private String weightName;
    private List<Leg> legs;
    private Geometry geometry;
    private double distance;
    private long duration;
    private long weight;

    @JsonProperty("weight_name")
    public String getWeightName()
    {
        return weightName;
    }

    @JsonProperty("weight_name")
    public void setWeightName(String value)
    {
        this.weightName = value;
    }

    @JsonProperty("legs")
    public List<Leg> getLegs()
    {
        return legs;
    }

    @JsonProperty("legs")
    public void setLegs(List<Leg> value)
    {
        this.legs = value;
    }

    @JsonProperty("geometry")
    public Geometry getGeometry()
    {
        return geometry;
    }

    @JsonProperty("geometry")
    public void setGeometry(Geometry value)
    {
        this.geometry = value;
    }

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

    @JsonProperty("duration")
    public long getDuration()
    {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(long value)
    {
        this.duration = value;
    }

    @JsonProperty("weight")
    public long getWeight()
    {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(long value)
    {
        this.weight = value;
    }
}
