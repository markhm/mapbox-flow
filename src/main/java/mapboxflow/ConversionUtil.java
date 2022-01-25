package mapboxflow;

import com.github.markhm.mapbox.style.Geometry;

public class ConversionUtil {
    public static mapboxflow.jsonobject.layer.Geometry convert(Geometry fromGeometry) {
        mapboxflow.jsonobject.layer.Geometry result = new mapboxflow.jsonobject.layer.Geometry(mapboxflow.jsonobject.layer.Geometry.Type.LineString);
        result.setCoordinates(fromGeometry.getCoordinates());

        return result;
    }

}
