package com.github.markhm.mapbox;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeoLocation
{
    private String name = null;

    private double latitude;
    private double longitude;

    /** GeoLocations are created [latitude, longtitude]
     *
     * @param latitude
     * @param longitude
     */
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
        if (name != null)
        {
            return name + " - " + getLongLat();
        }
        else
        {
            return getLongLat();
        }
    }

    public String getName()
    {
        return name;
    }

    public String getLongLat()
    {
        return "[" + longitude + "," + latitude+ "]";
    }

    // @Deprecated // This should never be necessary
    public String getLatLong()
    {
        return "[" + latitude + "," + longitude+ "]";
    }

    public double[] getCoordArray()
    {
        double[] result = new double[2];

        result[0] = longitude;
        result[1] = latitude;

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

    public static GeoLocation InitialView_Turku_NY = new GeoLocation("Initial view Turku, NY",50, -26);
    public static GeoLocation InitialView_AMS_CPH = new GeoLocation("Initial view AMS, CPH",54, 8.74);

    // Europe
    public static GeoLocation Turku = new GeoLocation("Turku",60.45, 22.266667);

    public static GeoLocation Paris = new GeoLocation("Paris", 48.8566, 2.3522);

    public static GeoLocation Amsterdam = new GeoLocation("Amsterdam", 52.3702, 4.8952);
    public static GeoLocation Utrecht = new GeoLocation("Utrecht", 52.09083, 5.12222);

    public static GeoLocation Copenhagen = new GeoLocation("Copenhagen", 55.6761, 12.5683);
    public static GeoLocation Roskilde = new GeoLocation("Roskilde", 55.64152, 12.08035);


    public static GeoLocation Madrid = new GeoLocation("Madrid", 40.416775, -3.703790);
    public static GeoLocation Moscow = new GeoLocation("Moscow", 55.755825, 37.617298 );

    public static GeoLocation Oisterwijk = new GeoLocation("Oisterwijk", 51.5809686, 5.197761);
    public static GeoLocation Kijkduin = new GeoLocation("Kijkduin", 52.068889, 4.222388);

    // US
    public static GeoLocation Boston = new GeoLocation("Boston MA",  42.361145, -71.057083);
    public static GeoLocation NewYork = new GeoLocation("New York NY",  40.730610, -73.935242);
    public static GeoLocation NewYork_JFK = new GeoLocation("New York JFK",  40.627947, -73.771702);
    public static GeoLocation SanFrancisco = new GeoLocation("San Francisco", 37.776, -122.414);
    public static GeoLocation WashingtonDC = new GeoLocation("Washington DC", 38.913, -77.032 );

}
