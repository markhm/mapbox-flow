package com.github.markhm.mapbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.markhm.mapbox.directions.Converter;
import com.github.markhm.mapbox.layer.Source;
import com.github.markhm.mapbox.layer.SourceConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Route("dk_map")
public class DKKommunerView extends VerticalLayout
{
    private static Log log = LogFactory.getLog(DKKommunerView.class);

    private MapboxMap mapboxMap = null;

    boolean alreadyRendered = false;

    public DKKommunerView()
    {
        if (!alreadyRendered)
        {
            render();
        }
    }

    private void render()
    {
        H3 title = new H3("Danmarks Kommuner");
        add(title);

        addTopButtons();

        mapboxMap = new MapboxMap(GeoLocation.InitialView_Denmark, 6, false);
        add(mapboxMap);

        addBottomButtons();
    }

    public void referencingGeneratedClasses()
    {
        Converter converter = null;
    }


    private void addTopButtons()
    {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        Button zoomAarhus = new Button("Aarhus", e -> mapboxMap.zoomTo(GeoLocation.Aarhus, 12));
        Button zoomAalborg = new Button("Aalborg", e -> mapboxMap.zoomTo(GeoLocation.Aalborg, 12));
        Button zoomBornholm = new Button("Bornholm", e -> mapboxMap.zoomTo(GeoLocation.Bornholm, 10));
        Button zoomCopenhagen = new Button("Copenhagen", e -> mapboxMap.zoomTo(GeoLocation.Copenhagen, 10));
        Button zoomSkagen = new Button("Skagen", e -> mapboxMap.zoomTo(GeoLocation.Skagen, 12));
        Button zoomParis = new Button("Odense", e -> mapboxMap.zoomTo(GeoLocation.Odense, 14));

        Button zoomDenmark = new Button("Denmark", e -> mapboxMap.zoomTo(GeoLocation.InitialView_Denmark, 6));

        horizontalLayout.add(new Label("Zoom til:"), zoomAarhus, zoomAalborg, zoomBornholm, zoomCopenhagen, zoomSkagen, zoomParis, zoomDenmark);

        add(horizontalLayout);
    }

    private void addBottomButtons()
    {
        HorizontalLayout defaultButtons = new HorizontalLayout();
        defaultButtons.setAlignItems(Alignment.CENTER);

        Button addWireframe = new Button("Trådramme", e -> addSource());
        Button addSelection = new Button("Udvalgte kommuner", e -> addSelection());

        defaultButtons.add(new Label("Tilføj:"), addWireframe, addSelection);

        add(defaultButtons);
    }

    private void addSource()
    {
        String sourceString = getSourceString();

        String sourceCommand = "map.addSource('danske-kommuner', " + sourceString + ");";
        mapboxMap.executeJs(sourceCommand);

        String layerCommand = "map.addLayer({" +
                "'id': 'terrain-data'," +
                "'type': 'line'," +
                "'source': 'danske-kommuner'," +
                "'source-layer': 'Danske_Kommuner'," +
                "'layout': {" +
                "'line-join': 'round'," +
                "'line-cap': 'round'" +
                "}," +
                "'paint': " +
                "{" +
                "'line-color': '#ff69b4'," +
//                    "'fill-color': '#ff8ad0'," +
                "'line-width': 1" +
                "}" +
                "});";

        mapboxMap.executeJs(layerCommand);
    }

    private String getSourceString()
    {
        Source source = new Source();
        source.setType("vector");
        source.setUrl("mapbox://markhm.ck4aedo8f03l32nmyksha2171-5k3o4");

        String sourceString = null;
        try
        {
            sourceString = SourceConverter.toJsonString(source);
        }
        catch (JsonProcessingException jpe)
        {
            log.error(jpe);
        }
        return sourceString;
    }

    private void addSelection()
    {
        String command = "map.addLayer({" +
                "'id': 'color'," +
                "'type': 'fill'," +
                "'source': "+getSourceString()+"," +
                "'source-layer': 'Danske_Kommuner'," +
                "'paint': {" +
                "'fill-color': " +
                "[" +
                "'match'," +
                "['get', 'name']," +
                "[" +
                "'Skive'," +
                "'Haderslev'," +
                "'Esbjerg'," +
                "'Lejre'," +
                "'Køge'," +
                "'Nyborg'," +
                "'Brønderslev'," +
                "'Rødovre'," +
                "'Aarhus'," +
                "'Odder'" +
                "]," +
                "'hsla(271, 38%, 61%, 0.5)'," +
                "'hsla(0, 0%, 0%, 0)'" +
                "]" +
                "}" +
                "}, );";

        mapboxMap.executeJs(command);
    }


    public static Layer getExampleLayer()
    {
        // getLayer().toString() or getLayer().toString().replace("\"", "\'") is not needed
        Layer layer = new Layer("points", Layer.Type.symbol);

        Layer.Properties mapboxDCProperties = new Layer.Properties("National Bank", Sprite.Bank.toString());
        GeoLocation mapboxDCLocation = new GeoLocation(-77.03238901390978, 38.913188059745586);
        Layer.Feature mapboxDCFeature = new Layer.Feature("Feature", mapboxDCProperties, mapboxDCLocation);
        layer.addFeature(mapboxDCFeature);

        Layer.Properties mapboxDangerProperties = new Layer.Properties("National danger", Sprite.Danger.toString());
        GeoLocation mapboxDangerLocation = new GeoLocation(30, -20);
        Layer.Feature mapboxDangerFeature = new Layer.Feature("Feature", mapboxDangerProperties, mapboxDangerLocation);
        layer.addFeature(mapboxDangerFeature);

        Layer.Properties mapboxSFProperties = new Layer.Properties("Helicopter Haven", Sprite.Helicopter.toString());
        GeoLocation mapboxSFLocation = new GeoLocation(-122.414, 37.776);
        Layer.Feature mapboxSFFeature = new Layer.Feature("Feature", mapboxSFProperties, mapboxSFLocation);
        layer.addFeature(mapboxSFFeature);

        // System.out.println(layer.toString(2));

        return layer;
    }
}
