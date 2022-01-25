package mapboxflow.jsonobject.layer;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Geometry extends JSONObject implements Serializable {
    public Geometry(Type type) {
        put("type", type);
    }

//    public List<List<Double>> getCoordinates()
//    {
//        return (List<List<Double>>) get("coordinates");
//    }

    public void setCoordinates(List<List<Double>> value) {
        Object type = get("type");
        if (type.equals(Type.LineString)) {
            put("coordinates", value);
        } else if (type.equals(Type.Polygon)) {
            List<List<List<Double>>> list = new ArrayList<List<List<Double>>>();
            list.add(value);
            put("coordinates", list);
        }
    }

    public enum Type {
        Point, LineString, Polygon
    }

}
