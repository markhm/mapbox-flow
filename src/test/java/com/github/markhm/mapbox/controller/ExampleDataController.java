package com.github.markhm.mapbox.controller;

import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.util.Color;
import elemental.json.JsonObject;
import mapboxflow.elemental.json.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/data")
public class ExampleDataController extends HttpServlet {
    private int example = 0;

    public ExampleDataController() {

    }

    protected void doGet(HttpServletRequest reqest, HttpServletResponse response)
            throws ServletException, IOException {
        // response.getWriter().println("Hello World!");
        response.getWriter().println(getDataExample());
    }


    private String getDataExample() {
        JsonObject geometry = GeometryHelper.createGeometry(GeometryHelper.Type.Polygon);

        List<List<Double>> coordinates = new ArrayList<>();

        if (example == 0) {
            coordinates.add(GeoLocation.Bermuda_1_Florida.getCoordList());
            coordinates.add(GeoLocation.Bermuda_2_Bermuda.getCoordList());
            coordinates.add(GeoLocation.Bermuda_3_PuertoRico.getCoordList());
            example = 1;
        } else // example == 1
        {
            coordinates.add(GeoLocation.Amsterdam.getCoordList());
            coordinates.add(GeoLocation.Copenhagen.getCoordList());
            coordinates.add(GeoLocation.Berlin.getCoordList());
            example = 0;
        }

        GeometryHelper.setCoordinates(geometry, coordinates);

        JsonObject data = DataHelper.createData(DataHelper.Type.single);
        FeatureHelper.createFeature("Feature", PropertiesHelper.createProperties(), geometry);
        data.put("geometry", geometry);

        JsonObject source = SourceHelper.createSource();
        source.put("data", data);

        return data.toString();
    }

}
