package com.github.markhm.mapbox;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;


public class StaticViewUtil
{
    public static Label verticalWhitespace(int height)
    {
        return verticalWhitespace(height + "pt");
    }

    public static Label verticalWhitespace(String height)
    {
        Label whiteSpace = new Label("");
        whiteSpace.setHeight(height);
        whiteSpace.setVisible(true);
        return whiteSpace;
    }

    public static Label horizontalWhiteSpace(int width)
    {
        return horizontalWhiteSpace(width + "pt");
    }

    public static Label horizontalWhiteSpace(String height)
    {
        Label whiteSpace = new Label("");
        whiteSpace.setWidth(height);
        whiteSpace.setVisible(true);
        return whiteSpace;
    }

    public static void click(UI ui, Component... components)
    {
        Page page = ui.getPage();

        for (Component component : components)
        {
            page.executeJs("$0.click()", component.getElement());
        }
    }

    public static VerticalLayout centeredBoxedText(String... text)
    {
        return centeredBoxedText(600, text);
    }

    public static VerticalLayout centeredBoxedText(int width, String... text)
    {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER); //
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setWidth(width+"px");
        for (String textItem : text)
        {
            layout.add(new Label(textItem));
        }

        return layout;
    }

}
