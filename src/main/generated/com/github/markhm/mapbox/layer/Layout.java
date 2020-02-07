// Layout.java

package com.github.markhm.mapbox.layer;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Layout {
    private String lineJoin;
    private String lineCap;

    @JsonProperty("line-join")
    public String getLineJoin() { return lineJoin; }
    @JsonProperty("line-join")
    public void setLineJoin(String value) { this.lineJoin = value; }

    @JsonProperty("line-cap")
    public String getLineCap() { return lineCap; }
    @JsonProperty("line-cap")
    public void setLineCap(String value) { this.lineCap = value; }
}
