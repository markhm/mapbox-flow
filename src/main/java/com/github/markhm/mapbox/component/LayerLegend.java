package com.github.markhm.mapbox.component;

import com.github.markhm.mapbox.ViewUtil;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LayerLegend extends VerticalLayout
{
    public LayerLegend()
    {
        setWidth("200px");
        add(new H3("# serviceopkald"));
        add("(multiplicer med 1000)");
    }

    public void addLegendLine(int from, int to, String color)
    {
        HorizontalLayout legendLine = new HorizontalLayout();

        Label colorBox = new Label("️");
        colorBox.getElement().setProperty("innerHTML", "&block;");
        colorBox.setWidth("5px");
        colorBox.setHeight("5px");
        colorBox.getElement().getStyle().set("color", color);
        colorBox.getElement().getStyle().set("color-background", color);
        colorBox.setVisible(true);

        Label fromToLabel = new Label(from/1000 + " - " + to/1000);
        legendLine.add(colorBox, ViewUtil.horizontalWhiteSpace(5), fromToLabel);

        add(legendLine);
    }

    public void addLegendLineFrom(int from, String color)
    {
        HorizontalLayout legendLine = new HorizontalLayout();

        Label colorBox = new Label("️");
        colorBox.getElement().setProperty("innerHTML", "&block;");
        colorBox.setWidth("5px");
        colorBox.setHeight("5px");
        colorBox.getElement().getStyle().set("color", color);

        Label fromToLabel = new Label(from/1000 + " - ");
        legendLine.add(colorBox, ViewUtil.horizontalWhiteSpace(5), fromToLabel);

        add(legendLine);
    }

    public void addLegendLineTo(int to, String color)
    {
        HorizontalLayout legendLine = new HorizontalLayout();

        Label colorBox = new Label();
        colorBox.getElement().setProperty("innerHTML", "&block;");
        colorBox.setWidth("5px");
        colorBox.setHeight("5px");
        colorBox.getElement().getStyle().set("color", color);

        Label fromToLabel = new Label(" - " + to/1000);
        legendLine.add(colorBox, ViewUtil.horizontalWhiteSpace(5), fromToLabel);

        add(legendLine);
    }


}
