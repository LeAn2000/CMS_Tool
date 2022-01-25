package com.ChildMonitoringSystem.CMS.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "local_location")
public class LocationLocalTrack {

    @PrimaryKey
    @NonNull String DATE_LOG;
    String LONGTITUDE;
    String LATITUDE;
    String SERI_PHONE;

    public LocationLocalTrack(@NonNull String DATE_LOG, String LONGTITUDE, String LATITUDE, String SERI_PHONE) {
        this.DATE_LOG = DATE_LOG;
        this.LONGTITUDE = LONGTITUDE;
        this.LATITUDE = LATITUDE;
        this.SERI_PHONE = SERI_PHONE;

    }

    @NonNull
    public String getDATE_LOG() {
        return DATE_LOG;
    }

    public void setDATE_LOG(@NonNull String DATE_LOG) {
        this.DATE_LOG = DATE_LOG;
    }

    public String getLONGTITUDE() {
        return LONGTITUDE;
    }

    public void setLONGTITUDE(String LONGTITUDE) {
        this.LONGTITUDE = LONGTITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }
}
