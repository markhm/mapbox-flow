package com.github.markhm.mapbox.util;

import com.github.markhm.mapbox.MapboxMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AccessToken {
    private static Log log;

    private static Properties properties = new Properties();

    static {
        log = LogFactory.getLog(AccessToken.class);

        // https://www.mkyong.com/java/java-properties-file-examples/

        ClassLoader latestClassLoader = MapboxMap.class.getClassLoader();

        try (InputStream defaultInputStream = latestClassLoader.getResourceAsStream("mapbox_default.properties")) {
            InputStream specificInputStream = latestClassLoader.getResourceAsStream("mapbox.properties");

            properties = new Properties();

            if (defaultInputStream != null) {
                // load a properties file
                properties.load(defaultInputStream);
                log.info("Loaded mapbox_default.properties file.");
            }
            if (specificInputStream != null) {
                properties.load(specificInputStream);
                log.info("Overridden by specific mapbox.properties file.");
            }

        } catch (IOException ex) {
            log.error("Something went wrong reading properties file: " + ex.getMessage());
            log.error("Did you create an account at Mapbox.com and save your API key in src/main/resources/mapbox.properties...?");
            ex.printStackTrace(System.err);
        }
    }

    public static String getToken() {
        return properties.getProperty("mapboxgl.accessToken");
    }

    public static String getJSFileLocation() {
        String jsFileLocation = properties.getProperty("mapbox.jsfile");
        // log.info("Loading mapbox.js file from: "+jsFileLocation);
        return jsFileLocation;
    }

    public static String getCSSFileLocation() {
        String cssFileLocation = properties.getProperty("mapbox.cssfile");
        // log.info("Loading mapbox.css file from: "+cssFileLocation);
        return cssFileLocation;
    }

}
