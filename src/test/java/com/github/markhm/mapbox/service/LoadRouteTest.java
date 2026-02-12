package com.github.markhm.mapbox.service;

import com.github.markhm.mapbox.style.DirectionsResponseLoader;
import com.github.markhm.mapbox.style.Geometry;
import junit.framework.TestCase;
import mapboxflow.jsonobject.layer.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoadRouteTest extends TestCase {

    private static Log log = LogFactory.getLog(LoadRouteTest.class);

    public void testLoadRoute() {
        Geometry routeForCar_1 = DirectionsResponseLoader.createDirectionsResponseFrom(DirectionsResponseLoader.UTR_TO_ROS).getRoutes().get(0).getGeometry();
        log.info(routeForCar_1);
        log.info("******************************************************************");
        Layer lineLayer_1 = LineBuilder.getLineLayer("route_line_1", routeForCar_1);
        log.info(lineLayer_1);
    }
}
