package mapboxflow.jsonobject.layer;

import org.json.JSONException;
import org.json.JSONObject;

public class Layer extends JSONObject {
    public Layer(String id, Type type) {
        // id and type
        put("id", id);
        put("type", type);

        // layout
        setLayout(new Layout(type));
    }

    public void setPaint(Paint paint) {
        put("paint", paint);
    }

    public void setSourceId(String sourceId) {
        put("source", sourceId);
    }

    public String getId() {
        return getString("id");
    }

    public void setLayout(Layout layout) {
        put("layout", layout);
    }

    public void setSource(Source source) {
        put("source", source);
    }

    public void addFeature(Feature feature) {
        // source
        Source source = getSource();
        Data data = source.getData();
        if (data == null) {
            data = new Data(Data.Type.collection);
            source.setData(data);
        }
        data.addFeature(feature);
    }

    public Source getSource() {
        Source source = null;
        try {
            source = (Source) get("source");
        } catch (JSONException jsonException) {
            source = new Source();
            setSource(source);
        }

        return source;
    }

    public enum Type {
        symbol, line, fill, circle, heatmap, raster, hillshade, background; //, fill-extrusion
    }

}
