package com.github.markhm.mapbox.methods;

import com.github.markhm.mapbox.GeoLocation;

public class FlyToMethod {
    private static final String methodSignature = "this.map.flyTo";

    private GeoLocation location;
    private int zoomLevel;
    private int duration;

    public FlyToMethod(GeoLocation location, int duration) {
        this.location = location;
        this.duration = duration;
    }

    public FlyToMethod(GeoLocation location, int zoomLevel, int duration) {
        this.location = location;
        this.zoomLevel = zoomLevel;
        this.duration = duration;
    }

    private String toJSON() {
        if (zoomLevel != 0) {
            return "{center: " + location + ", zoom: " + this.zoomLevel + ", duration: " + this.duration + "}";
        }
        else {
            return "{center: " + location + ", duration: " + this.duration + "}";
        }
    }

    public String toString() {
        return methodSignature + "(" + toJSON() + ");";
    }

}
