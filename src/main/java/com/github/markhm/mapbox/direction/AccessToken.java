package com.github.markhm.mapbox.direction;

import com.github.markhm.mapbox.MapboxMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AccessToken
{
    public static String loadToken()
    {
        String token = "";

        // https://www.mkyong.com/java/java-properties-file-examples/
        try (InputStream input = MapboxMap.class.getClassLoader().getResourceAsStream("mapbox.properties"))
        {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            token = prop.getProperty("mapboxgl.accessToken");

            // get the property value and print it out
            // System.out.println("Successfully loaded access token from mapbox.properties file.");

        } catch (IOException ex)
        {
            System.err.println("Something went wrong reading properties file: "+ex.getMessage());
            System.err.println("Did you create an account at Mapbox.com and save your API key in src/main/resources/mapbox.properties...?");
            ex.printStackTrace(System.err);
        }

        return token;
    }
}
