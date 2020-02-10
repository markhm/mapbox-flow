package com.github.markhm.mapbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.internal.JsonSerializer;
import elemental.json.JsonValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


//@Tag("map")
//@JavaScript("./mapbox.js")
//@CssImport("./mapbox.css")
public class MapboxMap extends Div
{
    private static Log log = LogFactory.getLog(MapboxMap.class);

    private Page page = null;

    boolean alreadyRendered = false;

    private GeoLocation initialView = null;
    private int initialZoom;
    boolean dkMap = false;

    private MapboxOptions options = null;

    private MapboxMap()
    {
        setId("map");
        getStyle().set("align-self", "center");
        getStyle().set("border", "1px solid black");

        setWidth("1200px");
        setHeight("700px");

        page = UI.getCurrent().getPage();
    }

    public MapboxMap(MapboxOptions options)
    {
        this();

        this.options = options;
    }

    public MapboxMap(GeoLocation initialView, int initialZoom)
    {
        this(initialView, initialZoom, false);
    }


    public MapboxMap(GeoLocation initialView, int initialZoom, boolean dkMap)
    {
        this();
        // log.info("Entering Mapbox(..) constructor");
        this.initialView = initialView;
        this.initialZoom = initialZoom;
        this.dkMap = dkMap;

        if (!alreadyRendered)
        {
            render(dkMap);
            alreadyRendered = true;
        }
    }

    private void render(boolean dkMap)
    {
        // mapbox://styles/markhm/ck63rxdzn0zgq1iomoub9gk91

        // log.info("About to render()");
//        String jsFileLocation = AccessToken.getJSFileLocation();
//        String cssLocation = AccessToken.getCSSFileLocation();

        page.addStyleSheet("https://api.tiles.mapbox.com/mapbox-gl-js/v1.7.0/mapbox-gl.css");
        page.addJavaScript("https://api.tiles.mapbox.com/mapbox-gl-js/v1.7.0/mapbox-gl.js");
        page.addJavaScript("https://api.tiles.mapbox.com/mapbox.js/plugins/turf/v2.0.0/turf.min.js");

        page.addStyleSheet("./mapbox.css");
        page.addJavaScript("./mapbox.js");

        String accessToken = AccessToken.getToken();

        executeJs("mapboxgl.accessToken = '" + accessToken + "';");

        // render mapbox
//        options = new MapboxOptions();
//        options.setInitialZoom(initialZoom);
//        options.setInitialView(initialView);

        // log.info("Mapbox options: " + options);

        // JsonValue jsonString = JsonSerializer.toJson(options);

        // executeJs("renderCustomMap(" + options.toString().replace("\"", "'") + ");");
        // executeJs("renderCustomMap(" + options.toString() + ");");

        String styleString = getMapStyle(dkMap);

        // log.info("styleString = "+styleString);

        executeJs("renderCustomMap('" + styleString + "', " + initialView.getLongLat() + "," + initialZoom + ");");

        // add full screen control
        executeJs("map.addControl(new mapboxgl.FullscreenControl());");
    }

    private String getMapStyle(boolean dkMap)
    {
        if (dkMap)
        {
            return "mapbox://styles/markhm/ck4b4hiy41bmh1ck5ns089mhh";
        }
        else
        {
            return "mapbox://styles/mapbox/streets-v11";
        }
    }


    public void addAnimatedItem(AnimatedItem animatedItem)
    {
        Layer carLayer = new Layer(animatedItem.getLayerId(), "symbol");

        GeoLocation initialPosition = animatedItem.getLocation();
        log.info("Adding " + animatedItem.getDescription() + " to initial position " + initialPosition + " on map.");

        Layer.Properties carProperties = new Layer.Properties("", animatedItem.getSprite().toString());
        Layer.Feature carFeature = new Layer.Feature("Feature", carProperties, initialPosition);
        carLayer.addFeature(carFeature);

        log.info("CARLAYER: "+carLayer); // see below

        executeJs("addLayer(" + carLayer + ");");

//        UI ui = getUI().get();
//        ui.access(() ->
//        {
//
//        });
    }

//    {
//        "layout":
//        {
//            "text-field":["get","title"],
//            "text-offset":[0,0.7],
//            "text-anchor":"top",
//                "icon-image":["concat",["get","icon"],"-15"],
//            "text-font":["Open Sans  Semibold"]
//        },
//        "id":"cars",
//            "source":
//        {
//            "data":
//            {
//                "features":
//                        [{"geometry":
//                {
//                    "coordinates":[5.55361,52.026443],
//                    "type":"Point"
//                },
//                "type":"Feature",
//                        "properties": {"icon":"car","title":""}}
//                        ],
//                "type":"FeatureCollection"
//            },
//            "type":"geojson"
//        },
//        "type":"symbol"
//    }

    public void addLine(Geometry geometry, Color color)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerFor(Geometry.class);

        String geometryAsString = null;

        try
        {
            geometryAsString = writer.writeValueAsString(geometry);
        }
        catch (JsonProcessingException jpe)
        {
            log.error(jpe);
        }

        executeJs("addLine(" + geometryAsString + ", " + color.toStringForJS() + ");");
    }

//        {
//            "id":"routeLine",
//                "type":"line",
//                "source":
//            {
//                "type":"geojson",
//                    "data":
//                {
//                    "type":"Feature",
//                        "properties":{},
//                    "geometry":
//                    {
//                        "coordinates":[
//                    [55.755825,37.617298],
//                    [55.6761,12.5683]
//                    ],
//                        "type":"LineString"
//                    }
//                }
//            },
//            "layout":{"line-join":"round","line-cap":"round"},
//            "paint":{"line-color":{"line-color":"#377E21"},"line-width":3}
//        }

    public void removeAnimatedItem(AnimatedItem animatedItem)
    {
        executeJs("removeLayer(" + animatedItem.getLayerId() + ");");
    }

    public void zoomTo(GeoLocation geoLocation, int zoomLevel)
    {
        executeJs("map.flyTo({center: " + geoLocation.getLongLat() + ", zoom: " + zoomLevel + "});");
    }

    public void zoomTo(GeoLocation geoLocation)
    {
        executeJs("map.flyTo({center: " + geoLocation.getLongLat() + "});");
        // zoomTo(geoLocation, 9);
    }

    public void zoomTo(int zoomLevel)
    {
        page.executeJs("zoomTo(" + zoomLevel + ");");
    }

    public void startAnimation()
    {
        executeJs("startAnimation();");
    }

    public void executeJs(String javaScript)
    {
        page.executeJs(javaScript);
    }

    public void drawOriginDestinationFlight(GeoLocation origin, GeoLocation destination)
    {
        executeJs("fromOriginToDestination(" + origin.getLongLat() + ", " + destination.getLongLat() + ");");
    }

    // Documents regarding importing
    // https://github.com/vaadin/flow/issues/6582
    // https://vaadin.com/forum/thread/14045163/how-to-pack-server-side-java-script-in-executable-jar-file
    // https://vaadin.com/docs/v14/flow/importing-dependencies/tutorial-ways-of-importing.html
    // https://vaadin.com/forum/thread/18059914

}
