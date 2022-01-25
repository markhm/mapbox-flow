package mapboxflow.elemental.json;

import com.github.markhm.mapbox.util.Color;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

public class PaintHelper {
    public static JsonObject createPaint(Type type) {
        JsonObject paint = Json.createObject();
        // paint.put("type", type.toString());

        return paint;
    }

    public static JsonObject createPaint(Type type, Color color, int lineWidth) {
        JsonObject paint = createPaint(type);

        setLineColor(paint, color);
        setLineWidth(paint, lineWidth);

        return paint;
    }

    public static void setLineWidth(JsonObject paint, int lineWidth) {
        paint.put("line-width", lineWidth);
    }

    public static void setFillOpacity(JsonObject paint, double fillOpacity) {
        paint.put("fill-opacity", fillOpacity);
    }

    public static void setFillColor(JsonObject paint, Color color) {
        paint.put("fill-color", color.getHexValue());
    }

    public static void setFillColor(JsonObject paint, JsonArray array) {
        paint.put("fill-color", array);
    }

    public static void setLineColor(JsonObject paint, Color color) {
        paint.put("line-color", color.getHexValue());
    }

    public static void setLineColor(JsonObject paint, String colorHexValue) {
        paint.put("line-color", colorHexValue);
    }

    public enum Type {
        symbol, line, fill;
    }

}
