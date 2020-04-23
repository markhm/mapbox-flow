// Layer.java

package com.github.markhm.mapbox.layer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Layer
{
    private String id;
    private String type;
    private Source source;
    private Layout layout;
    private Paint paint;

    @JsonProperty("id")
    public String getId()
    {
        return id;
    }

    @JsonProperty("id")
    public void setId(String value)
    {
        this.id = value;
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

    @JsonProperty("source")
    public Source getSource()
    {
        return source;
    }

    @JsonProperty("source")
    public void setSource(Source value)
    {
        this.source = value;
    }

    @JsonProperty("layout")
    public Layout getLayout()
    {
        return layout;
    }

    @JsonProperty("layout")
    public void setLayout(Layout value)
    {
        this.layout = value;
    }

    @JsonProperty("paint")
    public Paint getPaint()
    {
        return paint;
    }

    @JsonProperty("paint")
    public void setPaint(Paint value)
    {
        this.paint = value;
    }

//    public enum Type
//    {
//        fill, line, symbol, circle, heatmap, raster, hillshade, background; //, fill-extrusion
//    }
}