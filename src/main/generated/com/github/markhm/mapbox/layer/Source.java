// Source.java

package com.github.markhm.mapbox.layer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.markhm.mapbox.Data;

public class Source
{
    private String type;
    private Data data;
    private String url;

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

    @JsonProperty("data")
    public Data getData()
    {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data value)
    {
        this.data = value;
    }

    @JsonProperty("url")
    public void setUrl(String url)
    {
        this.url = url;
    }

    @JsonProperty("url")
    public String getUrl()
    {
        return this.url;
    }
}