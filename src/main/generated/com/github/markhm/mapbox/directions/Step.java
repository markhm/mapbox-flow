package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Step
{
    private List<Intersection> intersections;
    private String name;
    private double distance;
    private Maneuver maneuver;
    private long weight;
    private Geometry geometry;
    private long duration;
    private Mode mode;
    private DrivingSide drivingSide;
    private String ref;
    private String exits;
    private String destinations;

    @JsonProperty("intersections")
    public List<Intersection> getIntersections()
    {
        return intersections;
    }

    @JsonProperty("intersections")
    public void setIntersections(List<Intersection> value)
    {
        this.intersections = value;
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

    @JsonProperty("maneuver")
    public Maneuver getManeuver()
    {
        return maneuver;
    }

    @JsonProperty("maneuver")
    public void setManeuver(Maneuver value)
    {
        this.maneuver = value;
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

    @JsonProperty("mode")
    public Mode getMode()
    {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(Mode value)
    {
        this.mode = value;
    }

    @JsonProperty("driving_side")
    public DrivingSide getDrivingSide()
    {
        return drivingSide;
    }

    @JsonProperty("driving_side")
    public void setDrivingSide(DrivingSide value)
    {
        this.drivingSide = value;
    }

    @JsonProperty("ref")
    public String getRef()
    {
        return ref;
    }

    @JsonProperty("ref")
    public void setRef(String value)
    {
        this.ref = value;
    }

    @JsonProperty("exits")
    public String getExits()
    {
        return exits;
    }

    @JsonProperty("exits")
    public void setExits(String value)
    {
        this.exits = value;
    }

    @JsonProperty("destinations")
    public String getDestinations()
    {
        return destinations;
    }

    @JsonProperty("destinations")
    public void setDestinations(String value)
    {
        this.destinations = value;
    }
}
