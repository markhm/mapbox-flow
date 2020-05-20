package mapboxflow.jsonobject.layer;

import com.github.markhm.mapbox.GeoLocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import java.io.Serializable;

public class Feature extends JSONObject implements Serializable
{
    private static Log log = LogFactory.getLog(Feature.class);

    public static final String FEATURE = "Feature";

    private Feature(String type, Properties properties)
    {
        put("type", type);
        put("properties", properties);
    }

    public Feature(String type, Properties properties, GeoLocation geoLocation)
    {
        this(type, properties);

        JSONObject geometryObject = new JSONObject();
        geometryObject.put("type", "Point");
        geometryObject.put("coordinates", geoLocation.getCoordArray());

        put("geometry", geometryObject);
    }

    public Feature(String type, Properties properties, Geometry geometry)
    {
        this(type, properties);

        put("geometry", geometry);
    }

    public String getType()
    {
        return getString("type");
    }

    public Geometry getGeometry()
    {
        return (Geometry) get("geometry");
    }

    public Properties getProperty()
    {
        return (Properties) get("properties");
    }

    public String getId()
    {
        return getProperty().getString("id");
    }

    public void setLocation(GeoLocation location)
    {
        JSONObject geometryObject = new JSONObject();
        geometryObject.put("type", "Point");
        geometryObject.put("coordinates", location.getCoordArray());

        put("geometry", geometryObject);
    }




}