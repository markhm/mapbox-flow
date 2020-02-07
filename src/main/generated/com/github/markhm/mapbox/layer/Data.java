// Data.java

package com.github.markhm.mapbox.layer;

import java.util.*;
import com.fasterxml.jackson.annotation.*;
import com.github.markhm.mapbox.directions.Geometry;

public class Data {
    private String type;
    private Properties properties;
    private Geometry geometry;

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("properties")
    public Properties getProperties() { return properties; }
    @JsonProperty("properties")
    public void setProperties(Properties value) { this.properties = value; }

    @JsonProperty("geometry")
    public Geometry getGeometry() { return geometry; }
    @JsonProperty("geometry")
    public void setGeometry(Geometry value) { this.geometry = value; }
}