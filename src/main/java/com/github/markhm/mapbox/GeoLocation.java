package com.github.markhm.mapbox;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeoLocation
{
    private String name = "";
    private double latitude;
    private double longitude;

    public GeoLocation(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoLocation(String name, double latitude, double longitude)
    {
        this(latitude, longitude);
        this.name = name;
    }

    public static GeoLocation fromJSONWaypoint(JSONObject jsonObject)
    {
        String name = jsonObject.getString("name");

        JSONArray locationArray = jsonObject.getJSONArray("location");
        return new GeoLocation(name, locationArray.getDouble(1), locationArray.getDouble(0));
    }

    public String toString()
    {
        return name + " - " + getLongLat();
    }


    public String getName()
    {
        return name;
    }

    public String getLongLat()
    {
        return "[" + longitude + "," + latitude+ "]";
    }

    public double[] getCoordArray()
    {
        double[] result = new double[2];

        result[0] = latitude;
        result[1] = longitude;

        return result;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    // Map
    public static GeoLocation Center = new GeoLocation("World", 0, 0);

    public static GeoLocation InitialView = new GeoLocation("Initial view", 17.1733, 49.508);
    public static GeoLocation InitialView_Turku_NY = new GeoLocation("Initial view",50, -26);
    public static GeoLocation InitialView_AMS_CPH = new GeoLocation("Initial view",54, 8.74);

    // Europe
    public static GeoLocation Turku = new GeoLocation("Turku",60.45, 22.266667);

    public static GeoLocation Paris = new GeoLocation("Paris", 48.8566, 2.3522);
    public static GeoLocation Amsterdam = new GeoLocation("Amsterdam", 52.3702, 4.8952);
    public static GeoLocation Copenhagen = new GeoLocation("Copenhagen", 55.6761, 12.5683);
    public static GeoLocation Madrid = new GeoLocation("Madrid", 40.416775, -3.703790);
    public static GeoLocation Moscow = new GeoLocation("Moscow", 55.755825, 37.617298 );

    public static GeoLocation Circelhuset = new GeoLocation("Circelhuset", 55.902685, 12.475549 );
    public static GeoLocation Graafschap_Doetinchem = new GeoLocation("Hotel De Graafschap, Doetinchem", 51.964903, 6.2883468);

    public static GeoLocation Oisterwijk = new GeoLocation("Oisterwijk", 51.5809686, 5.197761);
    public static GeoLocation Stokske = new GeoLocation("Stokske", 51.5534789,5.1893911);
    public static GeoLocation Kijkduin = new GeoLocation("Kijkduin", 52.068889, 4.222388);

    // US
    public static GeoLocation Boston = new GeoLocation("Boston MA",  42.361145, -71.057083);
    public static GeoLocation NewYork = new GeoLocation("New York NY",  40.730610, -73.935242);
    public static GeoLocation NewYork_JFK = new GeoLocation("New York JFK",  40.627947, -73.771702);
    public static GeoLocation SanFrancisco = new GeoLocation("San Francisco", 37.776, -122.414);
    public static GeoLocation WashingtonDC = new GeoLocation("Washington DC", 38.913, -77.032 );

}
