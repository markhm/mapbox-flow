package com.github.markhm.mapbox.ui;

import com.github.markhm.mapbox.AccessToken;
import com.github.markhm.mapbox.GeoLocation;
import com.github.markhm.mapbox.MapboxMap;
import com.github.markhm.mapbox.ui.DemoView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "polymer")
public class PolymerTestView extends VerticalLayout
{
    private MapboxMap map = null;

    boolean alreadyRendered = false;

    public PolymerTestView()
    {
        setAlignItems(Alignment.CENTER);

        if (!alreadyRendered)
        {
            render();
            alreadyRendered = true;
        }
    }

    private void render()
    {
        VerticalLayout contentBox = new VerticalLayout();
        contentBox.setWidth("1200px");
        add(contentBox);

        contentBox.add(new H3("Mapbox-Flow based on Polymer - Vaadin v15.0.6"));

        String accessToken = AccessToken.getToken();
        map = new MapboxMap(accessToken, GeoLocation.NewYork);
        map.setWidth("1200px");
        map.setHeight("700px");

        contentBox.add(map);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button addLayerButton = new Button("Add Layer", e -> addLayer());
        horizontalLayout.add(addLayerButton);

        Button hideButton = new Button("Hide layer", e -> map.hideLayer("symbols"));
        horizontalLayout.add(hideButton);

        Button unhideButton = new Button("Unhide layer", e -> map.unhideLayer("symbols"));
        horizontalLayout.add(unhideButton);

        contentBox.add(horizontalLayout);
    }


    private void addLayer()
    {
        map.addLayer(DemoView.getExampleLayer());
    }

}
