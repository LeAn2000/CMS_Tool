package com.ChildMonitoringSystem.CMS.Model;

public class INFO_LOCATION {
    private int LOCATION_ID;
    private String DATE_LOCATION;
    private int X_loc, Y_loc;
    private String SERI_PHONE;

    public INFO_LOCATION(int LOCATION_ID, String DATE_LOCATION, int x_loc, int y_loc, String SERI_PHONE) {
        this.LOCATION_ID = LOCATION_ID;
        this.DATE_LOCATION = DATE_LOCATION;
        X_loc = x_loc;
        Y_loc = y_loc;
        this.SERI_PHONE = SERI_PHONE;
    }

    public int getLOCATION_ID() {
        return LOCATION_ID;
    }

    public void setLOCATION_ID(int LOCATION_ID) {
        this.LOCATION_ID = LOCATION_ID;
    }

    public String getDATE_LOCATION() {
        return DATE_LOCATION;
    }

    public void setDATE_LOCATION(String DATE_LOCATION) {
        this.DATE_LOCATION = DATE_LOCATION;
    }

    public int getX_loc() {
        return X_loc;
    }

    public void setX_loc(int x_loc) {
        X_loc = x_loc;
    }

    public int getY_loc() {
        return Y_loc;
    }

    public void setY_loc(int y_loc) {
        Y_loc = y_loc;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }
}
