package com.github.markhm.mapbox;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.Properties;

public class TestData
{
    public static JSONObject loadFile()
    {
        JSONObject result = null;
        ClassLoader latestClassLoader = MapboxMap.class.getClassLoader();

        try (InputStream defaultInputStream = latestClassLoader.getResourceAsStream("data/danske_kommuner.geo.json"))
        {

            String fileContents = IOUtils.toString(defaultInputStream, "UTF-8");
            result = new JSONObject(fileContents);

        } catch (IOException ex)
        {
            System.err.println("Something went wrong reading properties file: " + ex.getMessage());
            System.err.println("Did you create an account at Mapbox.com and save your API key in src/main/resources/mapbox.properties...?");
            ex.printStackTrace(System.err);
        }

        return result;
    }

}
