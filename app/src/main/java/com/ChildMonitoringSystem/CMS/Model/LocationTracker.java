package com.ChildMonitoringSystem.CMS.Model;

public class LocationTracker {
    String Seriphone;
    String Latitude;
    String Longtitude;
    String DateLog;

    public LocationTracker(String seriphone, String latitude, String longtitude, String datelog) {
        Seriphone = seriphone;
        Latitude = latitude;
        Longtitude = longtitude;
        DateLog = datelog;
    }

    public String getDateLog() {
        return DateLog;
    }

    public void setDateLog(String dateLog) {
        DateLog = dateLog;
    }

    public String getSeriphone() {
        return Seriphone;
    }

    public void setSeriphone(String seriphone) {
        Seriphone = seriphone;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(String longtitude) {
        Longtitude = longtitude;
    }
}
