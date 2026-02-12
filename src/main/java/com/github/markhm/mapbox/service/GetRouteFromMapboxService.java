package com.github.markhm.mapbox.service;

import com.github.markhm.mapbox.util.AccessToken;
import com.github.markhm.mapbox.GeoLocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GetRouteFromMapboxService extends AbstractRESTClient {
    private static Log log = LogFactory.getLog(GetRouteFromMapboxService.class);

    private String rootUrl = "https://api.mapbox.com/directions/v5/";

    public static String AMS_TO_CPH = "amsterdam_to_copenhagen.json";
    public static String PAR_TO_CPH = "paris_to_copenhagen.json";

    public GetRouteFromMapboxService(String accessToken) {
        super(accessToken);
    }

    // https://docs.mapbox.com/api-playground/#/directions/?_k=567jzz

    public static void main(String[] args) throws Exception {
        String accessToken = AccessToken.getToken();
        GetRouteFromMapboxService directionsRequest = new GetRouteFromMapboxService(accessToken);

        GeoLocation from = GeoLocation.Paris;
        GeoLocation to = GeoLocation.Copenhagen;

        String routeString = directionsRequest.getRoute(from, to);
        directionsRequest.writeRoute(PAR_TO_CPH, routeString);
    }

    public String getRoute(GeoLocation from, GeoLocation to) throws Exception {
        String origin = from.getLongitude() + "," + from.getLatitude();
        String destination = to.getLongitude() + "," + to.getLatitude();

        String combined = origin + ";" + destination;
        String encodedCombined = URLEncoder.encode(combined, StandardCharsets.UTF_8.toString());

        String requestAction = "mapbox/driving/" + encodedCombined + ".json";
        String result = doGetMethod(requestAction);
        return result;
    }

    public void writeRoute(String filename, String route) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));
        writer.write(route);
        writer.close();
        System.out.println("Result:");
        System.out.println("----------------------------------------------------");
        System.out.println(route);
    }

    public String getRootUrl() {
        return rootUrl;
    }

}
