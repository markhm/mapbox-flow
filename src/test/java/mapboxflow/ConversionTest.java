package mapboxflow;

import com.github.markhm.mapbox.service.LineBuilder;
import com.github.markhm.mapbox.style.DirectionsResponseLoader;
import com.github.markhm.mapbox.style.Geometry;
import junit.framework.TestCase;
import mapboxflow.jsonobject.layer.Layer;
import org.junit.jupiter.api.Test;

public class ConversionTest extends TestCase {

    @Test
    public void testConversation() {
        Geometry routeForCar_1 = DirectionsResponseLoader.createDirectionsResponseFrom(DirectionsResponseLoader.UTR_TO_ROS).getRoutes().get(0).getGeometry();
        assertNotNull(routeForCar_1);

        Layer lineLayer_1 = LineBuilder.getLineLayer("route_line_1", routeForCar_1);
        assertNotNull(lineLayer_1);
    }
}
