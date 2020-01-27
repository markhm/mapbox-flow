package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Lane
{
    private List<DrivingSide> indications;
    private boolean valid;

    @JsonProperty("indications")
    public List<DrivingSide> getIndications()
    {
        return indications;
    }

    @JsonProperty("indications")
    public void setIndications(List<DrivingSide> value)
    {
        this.indications = value;
    }

    @JsonProperty("valid")
    public boolean getValid()
    {
        return valid;
    }

    @JsonProperty("valid")
    public void setValid(boolean value)
    {
        this.valid = value;
    }
}
