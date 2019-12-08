package com.github.markhm.mapbox;

public class GeoLocation
{
    private String name;
    private double longitude;
    private double latitude;

    public GeoLocation(String name, double longitude, double latitude)
    {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName()
    {
        return name;
    }

    public String getCoordinates()
    {
        return "[" + longitude + "," + latitude + "]";
    }

    // Map
    public static GeoLocation Center = new GeoLocation("World", 0, 0);

    // Europe
    public static GeoLocation Turku = new GeoLocation("Turku",22.266667, 60.45);

    public static GeoLocation Paris = new GeoLocation("Paris", 2.3522, 48.8566);
    public static GeoLocation Amsterdam = new GeoLocation("Amsterdam", 4.8952, 52.3702);
    public static GeoLocation Copenhagen = new GeoLocation("Copenhagen", 12.5683, 55.6761);
    public static GeoLocation Madrid = new GeoLocation("Madrid", -3.703790, 40.416775);
    public static GeoLocation Moscow = new GeoLocation("Moscow", 37.617298, 55.755825);

    // US
    public static GeoLocation NewYork = new GeoLocation("New York NY",  -73.935242, 40.730610);
    public static GeoLocation SanFrancisco = new GeoLocation("San Francisco", -122.414, 37.776);
    public static GeoLocation WashingtonDC = new GeoLocation("Washington DC", -77.032, 38.913);

}
