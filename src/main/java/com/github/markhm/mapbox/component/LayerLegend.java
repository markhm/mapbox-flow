package com.github.markhm.mapbox.component;

import com.github.markhm.mapbox.util.ViewUtil;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LayerLegend extends VerticalLayout {
    public LayerLegend() {
        setWidth("200px");
        add(new H3("# serviceopkald"));
        add("(multiplicer med 1000)");
    }

    private Label getColorBox(String color, double opacity) {
        Label colorBox = new Label("️");
        colorBox.getElement().setProperty("innerHTML", "&block;");
        colorBox.setWidth("5px");
        colorBox.setHeight("5px");
        colorBox.getElement().getStyle().set("color", color);
        colorBox.getElement().getStyle().set("color-background", color);
        colorBox.getElement().getStyle().set("opacity", opacity + "");

        colorBox.setVisible(true);

        return colorBox;
    }

    public void addLegendLine(int from, int to, String color, double opacity) {
        HorizontalLayout legendLine = new HorizontalLayout();

        Label colorBox = getColorBox(color, opacity);

        Label fromToLabel = new Label(from / 1000 + " - " + to / 1000);
        legendLine.add(colorBox, ViewUtil.horizontalWhiteSpace(5), fromToLabel);

        add(legendLine);
    }

    public void addLegendLineFrom(int from, String color, double opacity) {
        HorizontalLayout legendLine = new HorizontalLayout();

        Label colorBox = getColorBox(color, opacity);

        Label fromToLabel = new Label(from / 1000 + " - ");
        legendLine.add(colorBox, ViewUtil.horizontalWhiteSpace(5), fromToLabel);

        add(legendLine);
    }

    public void addLegendLineTo(int to, String color, double opacity) {
        HorizontalLayout legendLine = new HorizontalLayout();

        Label colorBox = getColorBox(color, opacity);

        Label fromToLabel = new Label(" - " + to / 1000);
        legendLine.add(colorBox, ViewUtil.horizontalWhiteSpace(5), fromToLabel);

        add(legendLine);
    }


}
