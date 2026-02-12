package com.github.markhm.mapbox;

public class MapboxProperties {

    private String accessToken;

    private int flyToDelay = 1500;
    private int zoomToDelay = 1500;

    private GeoLocation initialLocation;
    private int initialZoom = 0;
    private boolean addFullScreenControl = true;

    public MapboxProperties(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public GeoLocation getInitialLocation() {
        return initialLocation;
    }

    public void setInitialLocation(GeoLocation initialLocation) {
        this.initialLocation = initialLocation;
    }

    public int getInitialZoom() {
        return initialZoom;
    }

    public void setInitialZoom(int initialZoom) {
        this.initialZoom = initialZoom;
    }

    public boolean isAddFullScreenControl() {
        return addFullScreenControl;
    }

    public void setAddFullScreenControl(boolean addFullScreenControl) {
        this.addFullScreenControl = addFullScreenControl;
    }

    public int getFlyToDelay() {
        return flyToDelay;
    }

    public void setFlyToDelay(int flyToDelay) {
        this.flyToDelay = flyToDelay;
    }

    public int getZoomToDelay() {
        return zoomToDelay;
    }

    public void setZoomToDelay(int zoomToDelay) {
        this.zoomToDelay = zoomToDelay;
    }

}
