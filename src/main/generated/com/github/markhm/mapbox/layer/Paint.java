// Paint.java

package com.github.markhm.mapbox.layer;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Paint {
    private String lineColor;
    private String lineWidth;

    @JsonProperty("line-color")
    public String getLineColor() { return lineColor; }
    @JsonProperty("line-color")
    public void setLineColor(String value) { this.lineColor = value; }

    @JsonProperty("line-width")
    public String getLineWidth() { return lineWidth; }
    @JsonProperty("line-width")
    public void setLineWidth(String value) { this.lineWidth = value; }
}