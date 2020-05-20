package com.github.markhm.mapbox.directions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.markhm.mapbox.RouteFragment;
import com.github.markhm.mapbox.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// https://app.quicktype.io/

public class DirectionsResponse
{
    private List<Route> routes;
    private List<Waypoint> waypoints;
    private String code;
    private String uuid;


    public List<RouteFragment> getRouteFragments()
    {
        return routeFragments;
    }

    public RouteFragment getLocation(int locationPointer)
    {
        return routeFragments.get(locationPointer);
    }

    @JsonProperty("routes")
    public List<Route> getRoutes()
    {
        return routes;
    }

    @JsonProperty("routes")
    public void setRoutes(List<Route> value)
    {
        this.routes = value;
    }

    @JsonProperty("waypoints")
    public List<Waypoint> getWaypoints()
    {
        return waypoints;
    }

    @JsonProperty("waypoints")
    public void setWaypoints(List<Waypoint> value)
    {
        this.waypoints = value;
    }

    @JsonProperty("code")
    public String getCode()
    {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String value)
    {
        this.code = value;
    }

    @JsonProperty("uuid")
    public String getUUID()
    {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setUUID(String value)
    {
        this.uuid = value;
    }

}
