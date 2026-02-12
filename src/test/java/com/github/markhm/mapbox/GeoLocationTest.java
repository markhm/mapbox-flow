package com.github.markhm.mapbox;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeoLocationTest {
    public GeoLocationTest() {
    }

    @Test
    public void testGeoLocation() {
        GeoLocation geoLocation = GeoLocation.InitialView_Denmark;
        String longLatString = geoLocation.getLongLat();
        GeoLocation newLocation = GeoLocation.fromLongLat(longLatString);

        Assertions.assertEquals(geoLocation, newLocation);

    }

}
