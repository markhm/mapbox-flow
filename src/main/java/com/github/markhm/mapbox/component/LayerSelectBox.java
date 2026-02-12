package com.github.markhm.mapbox.component;

import com.github.markhm.mapbox.MapboxMap;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

public class LayerSelectBox extends VerticalLayout {
    private static Log log = LogFactory.getLog(LayerSelectBox.class);

    // private MapboxMap mapboxMap = null;
    private MapboxMap mapboxMap = null;
    private MultiSelectListBox<String> listBox = null;

    private boolean alreadyRendered = false;

    private Set<String> selectableLayers = null;

    public LayerSelectBox(MapboxMap mapboxMap, Set<String> selectableLayers) {
        this.mapboxMap = mapboxMap;
        this.selectableLayers = selectableLayers;
        listBox = new MultiSelectListBox<>();
        listBox.setItems(selectableLayers);
        listBox.addValueChangeListener(e -> valueChanged(e));

        setWidth("200px");

        if (!alreadyRendered) {
            render();
            alreadyRendered = true;
        }
    }

    private void valueChanged(HasValue.ValueChangeEvent valueChangeEvent) {
        Set<String> activeLayers = (Set<String>) valueChangeEvent.getValue();

        for (String layer : selectableLayers) {
            if (activeLayers.contains(layer)) {
                mapboxMap.unhideLayer(layer);
            } else {
                mapboxMap.hideLayer(layer);
            }
        }
    }

    public void render() {
        add(new H4("Active layers"));
        add(listBox);
    }

    public void registerLayer(String layer) {
        if (!selectableLayers.contains(layer)) {
            selectableLayers.add(layer);
        }
        resetLayers();
    }

    public void resetLayers() {
        listBox.setItems(selectableLayers);
        listBox.setValue(selectableLayers);
    }
}
