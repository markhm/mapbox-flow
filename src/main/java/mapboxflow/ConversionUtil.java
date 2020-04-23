package mapboxflow;

import com.github.markhm.mapbox.Geometry;

public class ConversionUtil
{
    public static mapboxflow.layer.Geometry convert(Geometry fromGeometry)
    {
        mapboxflow.layer.Geometry result = new mapboxflow.layer.Geometry(mapboxflow.layer.Geometry.Type.LineString);
        result.setCoordinates(fromGeometry.getCoordinates());

        return result;
    }

}
