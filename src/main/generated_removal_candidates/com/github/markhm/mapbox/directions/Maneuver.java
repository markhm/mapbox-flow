package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Maneuver
{
    private long bearingAfter;
    private String type;
    private long bearingBefore;
    private List<Double> location;
    private String instruction;
    private DrivingSide modifier;
    private Long exit;

    @JsonProperty("bearing_after")
    public long getBearingAfter()
    {
        return bearingAfter;
    }

    @JsonProperty("bearing_after")
    public void setBearingAfter(long value)
    {
        this.bearingAfter = value;
    }

    @JsonProperty("type")
    public String getType()
    {
        return type;
    }

    @JsonProperty("type")
    public void setType(String value)
    {
        this.type = value;
    }

    @JsonProperty("bearing_before")
    public long getBearingBefore()
    {
        return bearingBefore;
    }

    @JsonProperty("bearing_before")
    public void setBearingBefore(long value)
    {
        this.bearingBefore = value;
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

    @JsonProperty("instruction")
    public String getInstruction()
    {
        return instruction;
    }

    @JsonProperty("instruction")
    public void setInstruction(String value)
    {
        this.instruction = value;
    }

    @JsonProperty("modifier")
    public DrivingSide getModifier()
    {
        return modifier;
    }

    @JsonProperty("modifier")
    public void setModifier(DrivingSide value)
    {
        this.modifier = value;
    }

    @JsonProperty("exit")
    public Long getExit() { return exit; }
    @JsonProperty("exit")
    public void setExit(Long value) { this.exit = value; }
}
