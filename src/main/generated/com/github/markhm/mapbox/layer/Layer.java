// Layer.java

package com.github.markhm.mapbox.layer;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Layer {
    private String id;
    private String type;
    private Source source;
    private Layout layout;
    private Paint paint;

    @JsonProperty("id")
    public String getID() { return id; }
    @JsonProperty("id")
    public void setID(String value) { this.id = value; }

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("source")
    public Source getSource() { return source; }
    @JsonProperty("source")
    public void setSource(Source value) { this.source = value; }

    @JsonProperty("layout")
    public Layout getLayout() { return layout; }
    @JsonProperty("layout")
    public void setLayout(Layout value) { this.layout = value; }

    @JsonProperty("paint")
    public Paint getPaint() { return paint; }
    @JsonProperty("paint")
    public void setPaint(Paint value) { this.paint = value; }
}