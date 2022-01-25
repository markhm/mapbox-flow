package mapboxflow.elemental.json;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.List;

public class GeometryHelper {
    public static JsonObject createGeometry(Type type) {
        JsonObject geometry = Json.createObject();
        geometry.put("type", type.toString());

        return geometry;
    }

    public static void setCoordinates(JsonObject geometry, List<List<Double>> coordinatesList) {
        JsonArray coordinatesListArray = toJsonArray(coordinatesList);

        String type = geometry.getString("type");

        if (type.equals(Type.LineString.toString())) {
            geometry.put("coordinates", coordinatesListArray);
        } else if (type.equals(Type.Polygon.toString())) {
            JsonArray polygonArray = Json.createArray();
            polygonArray.set(0, coordinatesListArray);

            geometry.put("coordinates", polygonArray);
        }
    }

    public static JsonArray toJsonArray(List<List<Double>> coordinatesList) {
        JsonArray coordinatesListArray = Json.createArray();

        if (coordinatesList.size() == 1) {
            List<Double> coordinates = coordinatesList.get(0);
            for (int j = 0; j < coordinates.size(); j++) {
                double coordinateElement = coordinates.get(j);
                coordinatesListArray.set(j, coordinateElement);
            }
        } else {
            for (int i = 0; i < coordinatesList.size(); i++) {
                JsonArray coordinatesArray = Json.createArray();
                List<Double> coordinates = coordinatesList.get(i);
                for (int j = 0; j < coordinates.size(); j++) {
                    double coordinateElement = coordinates.get(j);
                    coordinatesArray.set(j, coordinateElement);
                }
                coordinatesListArray.set(i, coordinatesArray);
            }
        }

        return coordinatesListArray;
    }


    public enum Type {
        Point, LineString, Polygon
    }
}
