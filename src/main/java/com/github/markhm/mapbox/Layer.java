package com.github.markhm.mapbox;

import org.json.JSONArray;
import org.json.JSONObject;

public class Layer extends JSONObject
{
    private JSONArray features = null;

    public Layer(String id, String type)
    {
        // id and type
        put("id", id);
        put("type", type);

        // source
        addSource();

        // layout
        addLayout(new Layout());
    }

    public String getId()
    {
        return getString("id");
    }

    private void addSource()
    {
        // source
        features = new JSONArray();

        JSONObject sourceObject = new JSONObject();
        sourceObject.put("type", "geojson");

        JSONObject dataObject = new JSONObject();
        dataObject.put("type", "FeatureCollection");
        dataObject.put("features", features);

        sourceObject.put("data", dataObject);

        put("source", sourceObject);
    }

    private void addLayout(Layout layout)
    {
        put("layout", layout);
    }

    public void addFeature(Feature feature)
    {
        int currentLength = features.length();
        features.put(currentLength, feature);
    }

    public static class Feature extends JSONObject
    {
        public Feature(String type, Properties properties, GeoLocation geoLocation)
        {
            this.put("type", "Feature");
            this.put("properties", properties);

            JSONObject geometryObject = new JSONObject();
            geometryObject.put("type", "Point");
            geometryObject.put("coordinates", geoLocation.getCoordArray());

            this.put("geometry", geometryObject);
        }
    }

    public static class Properties extends JSONObject
    {
        public Properties(String title, String icon)
        {
            put("title", title);
            put("icon", icon);
        }
    }

    public static class Layout extends JSONObject
    {
        public Layout()
        {
            JSONArray iconImageArray = new JSONArray();
            iconImageArray.put(0, "concat");
            JSONArray innerIconImageArray = new JSONArray();
            innerIconImageArray.put(0, "get");
            innerIconImageArray.put(1, "icon");
            iconImageArray.put(1, innerIconImageArray);
            iconImageArray.put(2, "-15");
            put("icon-image", iconImageArray);

            JSONArray textFieldArray = new JSONArray();
            textFieldArray.put(0, "get");
            textFieldArray.put(1, "title");
            put("text-field", textFieldArray);

            JSONArray textFontArray = new JSONArray();
            textFontArray.put(0, "Open Sans Semibold");
//            textFontArray.put(1, "Open Sans Semibold");
            // textFontArray.put(1, "Arial Unicode MS Bold");
            put("text-font", textFontArray);
            // put("text-font", "Open Sans Semibold");

            JSONArray textOffsetArray = new JSONArray();
            textOffsetArray.put(0, 0);
            textOffsetArray.put(1, 0.7);
            put("text-offset", textOffsetArray);

            put("text-anchor", "top");
        }
    }
}
