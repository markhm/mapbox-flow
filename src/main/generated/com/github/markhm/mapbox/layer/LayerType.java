package com.github.markhm.mapbox.layer;

public enum LayerType {
    BACKGROUND("background"),
    FILL("fill"),
    LINE("line"),
    SYMBOL("symbol"),
    RASTER("raster"),
    CIRCLE("circle"),
    FILL_EXTURSION("fill-extursion"),
    HEATMAP("heatmap"),
    HILLSHARE("hillshade"),
    SKY("sky");

    private String type;

    private LayerType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
