package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Leg
{
    private String summary;
    private List<Step> steps;
    private double distance;
    private long duration;
    private long weight;

    @JsonProperty("summary")
    public String getSummary()
    {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String value)
    {
        this.summary = value;
    }

    @JsonProperty("steps")
    public List<Step> getSteps()
    {
        return steps;
    }

    @JsonProperty("steps")
    public void setSteps(List<Step> value)
    {
        this.steps = value;
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
