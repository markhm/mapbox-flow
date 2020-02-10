package com.github.markhm.mapbox.service;

import com.github.markhm.mapbox.AccessToken;
import com.github.markhm.mapbox.GeoLocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DirectionsRequest extends AbstractRESTClient
{
    private static Log log = LogFactory.getLog(DirectionsRequest.class);

    private String rootUrl = "https://api.mapbox.com/directions/v5/";

    public DirectionsRequest(String accessToken)
    {
        super(accessToken);
    }

    // https://docs.mapbox.com/api-playground/#/directions/?_k=567jzz

    public static void main(String[] args) throws Exception
    {
        String accessToken = AccessToken.getToken();
        DirectionsRequest directionsRequest = new DirectionsRequest(accessToken);

//        GeoLocation from = GeoLocation.Paris;
//        GeoLocation to = GeoLocation.Copenhagen;

        GeoLocation from = GeoLocation.Veenendaal;
        GeoLocation to = GeoLocation.Nivaa;

        String origin =  from.getLongitude() + "," + from.getLatitude();
        String destination = to.getLongitude() + "," + to.getLatitude();

        String combined = origin + ";" + destination;
        String encodedCombined = URLEncoder.encode(combined, StandardCharsets.UTF_8.toString());

        String requestAction = "mapbox/driving/" + encodedCombined + ".json";
        String result = directionsRequest.doGetMethod(requestAction);

        System.out.println("Result:");
        System.out.println("----------------------------------------------------");
        System.out.println(result);
    }

    public String getRootUrl()
    {
        return rootUrl;
    }

}
