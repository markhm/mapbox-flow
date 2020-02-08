// Data.java

package com.github.markhm.mapbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.markhm.mapbox.Geometry;
import com.github.markhm.mapbox.layer.Properties;

public class Data
{
    private String type;
    private Properties properties;
    private Geometry geometry;

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

    @JsonProperty("properties")
    public Properties getProperties()
    {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(Properties value)
    {
        this.properties = value;
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
}