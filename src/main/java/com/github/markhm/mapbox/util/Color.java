package com.github.markhm.mapbox.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Color
{
    public static final Color RED = new Color("#EA3323");
    public static final Color ORANGE = new Color("#F2A93B");
    public static final Color YELLOW = new Color("#FCF250"); // original: #FEFF54
    public static final Color GREEN = new Color("#377E21");

    public static final Color NAVY_BLUE = new Color("#000080");

    public static final Color RED_LINE = new Color("#d54648");

    @JsonProperty("color")
    private String hexValue = null;

    public Color(String hexValue)
    {
        this.hexValue = hexValue;
    }

    public String toString()
    {
        return hexValue;
    }

    public String getHexValue()
    {
        return hexValue;
    }

    public String toStringForJS()
    {
        return "'" + getHexValue() + "'";
    }

    public Color fromStringforJS(String stringvalue)
    {
        Color color = new Color(stringvalue.substring(1, stringvalue.length() - 1));

        return color;
    }


}