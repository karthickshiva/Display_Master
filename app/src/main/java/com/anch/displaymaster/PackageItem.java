package com.anch.displaymaster;

import android.graphics.drawable.Drawable;

/**
 * Created by Karthick on 12/12/2015.
 */
public class PackageItem {
    private Drawable icon;
    private String name;
    private String packageName;

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setIcon(Drawable icon1) {
        icon = icon1;
    }

    public void setName(String name1) {
        name = name1;
    }

    public void setPackageName(String packageName1) {
        packageName = packageName1;
    }
}
