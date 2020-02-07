package com.github.markhm.mapbox;

public class Color
{
    public static final Color RED = new Color("#EA3323");
    public static final Color ORANGE = new Color("#F2A93B");
    public static final Color YELLOW = new Color("#FCF250"); // original: #FEFF54
    public static final Color GREEN = new Color("#377E21");

    public static final Color NAVY_BLUE = new Color("#000080");

    public static final Color RED_LINE = new Color("#d54648");

    private String hexValue = null;

    public Color(String hexValue)
    {
        this.hexValue = hexValue;
    }

    public String toString()
    {
        return hexValue;
    }

    public String toStringForJS()
    {
        return "'" + toString() + "'";
    }

}