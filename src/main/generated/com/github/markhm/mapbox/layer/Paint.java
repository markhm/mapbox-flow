// Paint.java

package com.github.markhm.mapbox.layer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.markhm.mapbox.Color;

public class Paint
{
    private Color lineColor;
    private int lineWidth;

    @JsonProperty("line-color")
    public Color getLineColor()
    {
        return lineColor;
    }

    @JsonProperty("line-color")
    public void setLineColor(Color value)
    {
        this.lineColor = value;
    }

    @JsonProperty("line-width")
    public int getLineWidth()
    {
        return lineWidth;
    }

    @JsonProperty("line-width")
    public void setLineWidth(int value)
    {
        this.lineWidth = value;
    }
}