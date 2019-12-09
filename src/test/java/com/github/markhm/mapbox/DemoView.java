package com.github.markhm.mapbox;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class DemoView extends VerticalLayout
{
    private MapboxMap mapboxMap = null;

    boolean alreadyRendered = false;

    public DemoView()
    {
        if (!alreadyRendered)
        {
            render();
        }
    }

    private void render()
    {
        H3 title = new H3("Mapbox Demo");
        add(title);

        addTopButtons();

        mapboxMap = new MapboxMap(GeoLocation.InitialView_Turku_NY, 2);
        add(mapboxMap);

        addBottomButtons();
    }

    private void addTopButtons()
    {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        Button zoomTurku = new Button("Turku", e -> mapboxMap.flyTo(GeoLocation.Turku));
        Button zoomCopenhagen = new Button("Copenhagen", e -> mapboxMap.flyTo(GeoLocation.Copenhagen));
        Button zoomAmsterdam = new Button("Amsterdam", e -> mapboxMap.flyTo(GeoLocation.Amsterdam));
        Button zoomParis = new Button("Paris", e -> mapboxMap.flyTo(GeoLocation.Paris));
        Button zoomNewYork = new Button("New York", e -> mapboxMap.flyTo(GeoLocation.NewYork));

        Button zoomWorld = new Button("World", e ->
        {
            mapboxMap.flyTo(GeoLocation.Center);
            mapboxMap.zoomTo(1);
        });

        horizontalLayout.add(new Label("Zoom to:"), zoomTurku, zoomCopenhagen, zoomAmsterdam, zoomParis, zoomNewYork, zoomWorld);

        add(horizontalLayout);
    }

    private void addBottomButtons()
    {
        HorizontalLayout layerLayout = new HorizontalLayout();
        layerLayout.setAlignItems(Alignment.CENTER);

        HorizontalLayout animationsLayout = new HorizontalLayout();
        animationsLayout.setAlignItems(Alignment.CENTER);

        Button startAnimation = new Button("Circle animation", e -> mapboxMap.startAnimation());
        Button addLayer = new Button("Add layer", e -> mapboxMap.executeJS("addLayer(" + getLayer().toString().replace("\"", "\'") + ");"));
        Button removeLayer = new Button("Remove layer", e -> mapboxMap.executeJS("removeLayer('" + getLayer().getId()+ "');"));
        // Button hideLayer = new Button("Hide layer", e -> mapboxMap.executeJS("hideLayer('" + getLayer().getId()+ "');"));
        Button unhideLayer = new Button("Unhide layer", e -> mapboxMap.executeJS("unhideLayer('" + getLayer().getId()+ "');"));
        Button turkuNewYork = new Button("From Turku to NewYork", e ->
        {
            mapboxMap.drawOriginDestinationFlight(GeoLocation.Turku, GeoLocation.NewYork);
        });
        turkuNewYork.setId("replay");

        layerLayout.add(addLayer, removeLayer, unhideLayer);
        animationsLayout.add(new Label("Animations:"), turkuNewYork, startAnimation);

        add(layerLayout);
        add(animationsLayout);
    }

    public static Layer getLayer()
    {
        Layer layer = new Layer("points", "symbol");

        Layer.Properties mapboxDCProperties = new Layer.Properties("Mapbox DC", "monument");
        GeoLocation mapboxDCLocation = new GeoLocation(-77.03238901390978, 38.913188059745586);
        Layer.Feature mapboxDCFeature = new Layer.Feature("Feature", mapboxDCProperties, mapboxDCLocation);
        layer.addFeature(mapboxDCFeature);

        Layer.Properties mapboxSFProperties = new Layer.Properties("Mapbox SF", "harbor");
        GeoLocation mapboxSFLocation = new GeoLocation(-122.414, 37.776);
        Layer.Feature mapboxSFFeature = new Layer.Feature("Feature", mapboxSFProperties, mapboxSFLocation);
        layer.addFeature(mapboxSFFeature);

        // System.out.println(layer.toString(2));

        return layer;
    }
}
