package com.github.markhm.mapbox.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;

@StyleSheet("./com/github/markhm/mapbox-flow/mapbox.css")
public class InfoBox extends Div
{
    public InfoBox()
    {
        setId("infoBox");
        setClassName("infoBox");

        setHeight("50px");
        setWidth("600px");

        add("Geo coordinate box");
    }

}
