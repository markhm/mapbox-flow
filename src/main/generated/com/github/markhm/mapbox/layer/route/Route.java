// Route.java

package com.github.markhm.mapbox.layer.route;

import com.fasterxml.jackson.annotation.*;
import com.github.markhm.mapbox.layer.Data;

public class Route {
    private String type;
    private Data data;

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("data")
    public Data getData() { return data; }
    @JsonProperty("data")
    public void setData(Data value) { this.data = value; }
}