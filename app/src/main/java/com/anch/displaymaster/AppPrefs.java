package com.anch.displaymaster;

/**
 * Created by Karthick on 12/13/2015.
 */

public class AppPrefs {
    private boolean enabled, tEnabled;
    private int backTime, brightness;

    public AppPrefs() {
        enabled = false;
        tEnabled = false;
        brightness = -1;
        backTime = -1;
    }

    public int getBackTime() {
        return backTime;
    }

    public void setBackTime(int backTime) {
        this.backTime = backTime;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isTEnabled() {
        return tEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setTEnabled(boolean enabled) {
        this.tEnabled = enabled;
    }
}
