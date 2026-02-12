package com.github.markhm.mapbox.methods;

public class ZoomToMethod {
    private static final String methodSignature = "this.map.zoomTo";

    private int zoomLevel;
    private int duration;

    public ZoomToMethod(int zoomLevel, int duration) {
        this.zoomLevel = zoomLevel;
        this.duration = duration;
    }

    private String toJSON() {
        return zoomLevel + ", { duration: " + this.duration + " }";
    }

    public String toString() {
        return methodSignature + "(" + toJSON() + ");";
    }

}
