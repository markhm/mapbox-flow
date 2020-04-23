// Properties.java

package com.github.markhm.mapbox.layer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Properties
{
    private String title;
    private String icon;

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return icon;
    }

    public String getTitle()
    {
        return title;
    }
}