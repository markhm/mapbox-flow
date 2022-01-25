package mapboxflow.elemental.json;

import elemental.json.Json;
import elemental.json.JsonObject;

public class SourceHelper {
    public static JsonObject createSource() {
        JsonObject source = Json.createObject();
        source.put("type", "geojson");

        return source;
    }

    public void setType(JsonObject source, String type) {
        source.put("type", type);
    }

    public void setId(JsonObject source, String id) {
        source.put("id", id);
    }

    public String getId(JsonObject source) {
        return source.getString("id");
    }


    public void setData(JsonObject source, JsonObject data) {
        source.put("data", data);
    }

    public JsonObject getData(JsonObject source) {
        return source.getObject("data");
    }
}
