package mapboxflow.elemental.json;

import com.github.markhm.mapbox.GeoLocation;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import mapboxflow.layer.Geometry;
import mapboxflow.layer.Properties;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeatureHelper
{
    public static JsonObject createFeature(String type, JsonObject properties)
    {
        JsonObject feature = Json.createObject();

        feature.put("type", type);
        feature.put("properties", properties);

        return feature;
    }

    public static JsonObject createFeature(String type, JsonObject properties, GeoLocation geoLocation)
    {
        JsonObject feature = createFeature(type, properties);

        List<Double> coordinates = geoLocation.getCoordList();
        List<List<Double>> coordinatesList = new ArrayList();
        coordinatesList.add(coordinates);

        JsonObject geoLocationObject = Json.createObject();
        geoLocationObject.put("type", "Point");
        geoLocationObject.put("coordinates", GeometryHelper.toJsonArray(coordinatesList));

        feature.put("geometry", geoLocationObject);

        return feature;
    }

    public static JsonObject createFeature(String type, JsonObject properties, JsonObject geometry)
    {
        JsonObject feature = createFeature(type, properties);

        feature.put("geometry", geometry);
        return feature;
    }

    public static JsonObject getGeometry(JsonObject feature)
    {
        return feature.getObject("geometry");
    }


}
