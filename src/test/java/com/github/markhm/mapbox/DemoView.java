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

        mapboxMap = new MapboxMap();
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
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        Button startAnimation = new Button("Start animation", e -> mapboxMap.startAnimation());
        Button addLayer = new Button("Add layer", e -> mapboxMap.executeJS("addLayer();"));

        Button drawMadridMoscow = new Button("From Madrid to Moscow", e ->
        {
            mapboxMap.drawOriginDestinationFlight(GeoLocation.Madrid, GeoLocation.Moscow);
        });
        drawMadridMoscow.setId("replay");

        horizontalLayout.add(new Label("Work in progress:"), startAnimation, addLayer, drawMadridMoscow);

        add(horizontalLayout);
    }

}
