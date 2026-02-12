package mapboxflow.elemental.json;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

public class LayerHelper {
    public static JsonObject createLayer(String id, Type type) {
        JsonObject layer = Json.createObject();
        layer.put("id", id);
        layer.put("type", type.toString());

        JsonObject layout = Json.createObject();

        addLayoutType(layout, type);
        layer.put("layout", layout);

        return layer;
    }

    public static void addLayoutType(JsonObject layout, Type type) {
        if (type.equals(Type.line)) {
            initiateLine(layout);
        } else if (type.equals(Type.symbol)) {
            initiateSymbol(layout);
        } else if (type.equals(Type.fill)) {
            initiateFill(layout);
        } else {
            throw new RuntimeException("Please Layout for layer type " + type);
        }
    }

    private static void initiateFill(JsonObject layout) {
        // nothing prescribed
    }


    private static void initiateLine(JsonObject layout) {
        layout.put("line-join", "round");
        layout.put("line-cap", "round");
    }

    private static void initiateSymbol(JsonObject layout) {
        JsonArray iconImageArray = Json.createArray();
        iconImageArray.set(0, "concat");

        JsonArray innerIconImageArray = Json.createArray();
        innerIconImageArray.set(0, "get");
        innerIconImageArray.set(1, "icon");
        iconImageArray.set(1, innerIconImageArray);
        iconImageArray.set(2, "-15");
        layout.put("icon-image", iconImageArray);

        JsonArray textFieldArray = Json.createArray();
        textFieldArray.set(0, "get");
        textFieldArray.set(1, "title");

        layout.put("text-field", textFieldArray);

        JsonArray textFontArray = Json.createArray();
        textFontArray.set(0, "Open Sans Semibold");
//            textFontArray.put(1, "Open Sans Semibold");
        // textFontArray.put(1, "Arial Unicode MS Bold");
        layout.put("text-font", textFontArray);
        // put("text-font", "Open Sans Semibold");

        JsonArray textOffsetArray = Json.createArray();
        textOffsetArray.set(0, 0);
        textOffsetArray.set(1, 0.7);
        layout.put("text-offset", textOffsetArray);
        layout.put("text-anchor", "top");
    }

    public static void addFeature(JsonObject layer, JsonObject feature) {
        // source
        JsonObject source = getSource(layer);
        JsonObject data = source.getObject("data");
        if (data == null) {
            data = DataHelper.createData(DataHelper.Type.collection);
            source.put("data", data);
        }
        DataHelper.addFeature(data, feature);
    }

    public static JsonObject getSource(JsonObject layer) {
        JsonObject source = layer.getObject("source");

        if (source == null) {
            source = SourceHelper.createSource();
            layer.put("source", source);
        }

        return source;
    }

    public enum Type {
        symbol, line, fill, circle, heatmap, raster, hillshade, background; //, fill-extrusion
    }
}
