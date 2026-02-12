package com.github.markhm.mapbox.service;

import com.github.markhm.mapbox.util.AccessToken;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GeoCodingRequest extends AbstractRESTClient {
    private String rootUrl = "https://api.mapbox.com/geocoding/v5/";

    public GeoCodingRequest(String accessToken) {
        super(accessToken);
    }

    public static void main(String[] args) throws Exception {
        String accessToken = AccessToken.getToken();

        GeoCodingRequest geoCodingRequest = new GeoCodingRequest(accessToken);

        // String searchCriterion =  "Myremosevej 39, Nivå, Denmark";
        String searchCriterion = "Engvej 1b, 2960, Rungsted, Denmark";

        String encodedCriterion = URLEncoder.encode(searchCriterion, StandardCharsets.UTF_8.toString());

        String requestAction = "mapbox.places/" + encodedCriterion + ".json";

        String result = geoCodingRequest.doGetMethod(requestAction);

        System.out.println("Result:");
        System.out.println("----------------------------------------------------");
        System.out.println(result);
    }

    @Override
    public String getRootUrl() {
        return rootUrl;
    }
}
