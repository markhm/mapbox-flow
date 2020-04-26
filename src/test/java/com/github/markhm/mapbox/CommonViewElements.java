package com.github.markhm.mapbox;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.function.Consumer;

public class CommonViewElements
{
    private static int NOTIFICATION_DURATION = 7000;

    private static Log log = LogFactory.getLog(CommonViewElements.class);

    public CommonViewElements()
    {}

    public static void setGridLookAndfeel(Grid grid)
    {
        // grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
    }

    public static Component createHeader(String viewName)
    {
        HorizontalLayout header = new HorizontalLayout();
        header.add(new H3("KDI "+viewName));

        // header.setAlignItems(FlexComponent.Alignment.CENTER);

        header.setSpacing(true);
        header.setWidthFull();
        // header.setAlignItems(FlexComponent.Alignment.STRETCH);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return header;
    }

    public static void showOutput(String text, Component content, HasComponents outputContainer)
    {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }

    public static void openDialogWindow(VerticalLayout verticalLayout, int height, int width)
    {
        Dialog dialogWindow = new Dialog();
        dialogWindow.add(verticalLayout);

        dialogWindow.setWidth(width+"px");
        dialogWindow.setHeight(height+"px");
        dialogWindow.open();
    }

    public static void showErrorNotification(String text)
    {
        showErrorNotification(text, Notification.Position.BOTTOM_END);
    }

    public static void showErrorNotification(String text, Notification.Position position)
    {
        Notification notification = new ClickableNotification();
        notification.setText(text);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(NOTIFICATION_DURATION);
        // notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public static void showNotification(String text)
    {
        showNotification(text, Notification.Position.BOTTOM_START);
    }

    public static void showNotification(String text, Notification.Position position)
    {
        Notification notification = new ClickableNotification();
        notification.setText(text);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.setDuration(NOTIFICATION_DURATION);
        notification.setPosition(position);
        notification.open();
    }

    public static void showSuccessNotification(String text)
    {
        ClickableNotification notification = new ClickableNotification();

        // notification.addClickListener(notification);
        notification.setText(text);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setDuration(NOTIFICATION_DURATION);
        // notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public static class ClickableNotification extends Notification implements ClickNotifier<Notification>
    {
        public void onItemClicked(Object o)
        {
            log.info("Registered click");
            this.close();
        }
    }

    public static void clickLink(Anchor anchor)
    {
        // https://vaadin.com/forum/thread/17360172/vaadin-10-autmoatically-download-a-file-without-clicking-a-button

        UI ui = UI.getCurrent();
        Page page = ui.getPage();
        page.executeJs("$0.click()", anchor);
    }

//    public static void showWarningNotification(String text)
//    {
//        Notification notification = new Notification();
//        notification.setText(text);
//
//        NotificationVariant warningVariant = null;
//        try
//        {
//            Class<?> clazz = Class.forName("com.vaadin.flow.component.notification.NotificationVariant");
//            // Constructor<?> constructor = clazz.getConstructor(String.class);
//            // System.setSecurityManager(null);
//            Constructor[] constructors = clazz.getDeclaredConstructors();
//            for (Constructor<?> constructor : constructors)
//            {
//                if (Modifier.isPrivate(constructor.getModifiers()))
//                {
//                    constructor.setAccessible(true);
//                    Class<?>[] clazzs = constructor.getParameterTypes();
//                    warningVariant = (NotificationVariant) constructor.newInstance("warning");
//                    log.info("Successfully created new NotificationVariant via private constructor");
//
//                    // Probably not
//                    // https://stackoverflow.com/questions/9614282/how-to-create-an-instance-of-enum-using-reflection-in-java
//
////                    if (constructor.getParameterCount() == 1 && clazzs[0] == String.class)
////                    {
////                    }
//                }
//            }
////            if (constructors != null && constructors.length > 0)
////            {
////                constructors[0].setAccessible(true);
////                warningVariant = (NotificationVariant) constructors[0].newInstance("warning");
////            }
//        }
//        catch(Exception e)
//        {
//            log.error(e);
//        }
//
//        notification.addThemeVariants(warningVariant);
//        notification.setDuration(5000);
//        // notification.setPosition(Notification.Position.MIDDLE);
//        notification.open();
//    }

}
