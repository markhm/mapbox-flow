// Layer.java

package com.github.markhm.mapbox.layer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Layer
{
    private String id;
    private LayerType type;
    private Source source;
    private Layout layout;
    private Paint paint;

    public Layer(String id, LayerType type) {
        this.id = id;
        this.type = type;
    }

    @JsonProperty("id")
    public String getId()
    {
        return id;
    }

    @JsonProperty("type")
    public String getType()
    {
        return type.toString();
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

}