package com.github.markhm.mapbox.component;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;

@CssImport("./com/github/markhm/mapbox-flow/mapbox.css")
public class InfoBox extends Div
{
    public InfoBox()
    {
        setId("infoBox");
        setClassName("infoBox");

        setHeight("35px");
        setWidth("400px");
    }

}
