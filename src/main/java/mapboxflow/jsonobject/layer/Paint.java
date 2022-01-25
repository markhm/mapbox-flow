package mapboxflow.jsonobject.layer;

import com.github.markhm.mapbox.util.Color;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Paint extends JSONObject implements Serializable {
    private Type type = null;

    public Paint(Type type) {
        this.type = type;
    }

    public Paint(Type type, Color color, int lineWidth) {
        this(type);

        setLineColor(color);
        setLineWidth(lineWidth);
    }

    public void setLineWidth(int lineWidth) {
        put("line-width", lineWidth);
    }

    public void setFillOpacity(double fillOpacity) {
        put("fill-opacity", fillOpacity);
    }

    public void setFillColor(Color color) {
        put("fill-color", color.getHexValue());
    }

    public void setFillColor(JSONArray array) {
        put("fill-color", array);
    }

    public void setLineColor(Color color) {
        put("line-color", color.getHexValue());
    }

    public void setLineColor(String colorHexValue) {
        put("line-color", colorHexValue);
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        line, fill;
    }
}
