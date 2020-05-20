package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Intersection
{
    private Long out;
    private List<Long> bearings;
    private List<Boolean> entry;
    private List<Double> location;
    private Long in;
    private List<Lane> lanes;
    private List<ClassElement> classes;

    @JsonProperty("out")
    public Long getOut()
    {
        return out;
    }

    @JsonProperty("out")
    public void setOut(Long value)
    {
        this.out = value;
    }

    @JsonProperty("bearings")
    public List<Long> getBearings()
    {
        return bearings;
    }

    @JsonProperty("bearings")
    public void setBearings(List<Long> value)
    {
        this.bearings = value;
    }

    @JsonProperty("entry")
    public List<Boolean> getEntry()
    {
        return entry;
    }

    @JsonProperty("entry")
    public void setEntry(List<Boolean> value)
    {
        this.entry = value;
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

    @JsonProperty("in")
    public Long getIn()
    {
        return in;
    }

    @JsonProperty("in")
    public void setIn(Long value)
    {
        this.in = value;
    }

    @JsonProperty("lanes")
    public List<Lane> getLanes()
    {
        return lanes;
    }

    @JsonProperty("lanes")
    public void setLanes(List<Lane> value)
    {
        this.lanes = value;
    }

    @JsonProperty("classes")
    public List<ClassElement> getClasses()
    {
        return classes;
    }

    @JsonProperty("classes")
    public void setClasses(List<ClassElement> value)
    {
        this.classes = value;
    }
}
