package com.ChildMonitoringSystem.CMS.Model;

public class INFO_APP {
    public String SERI_PHONE;
    public String APPNAME;
    public String pNAME;
    public String verNAME;
    public int verCODE;
    public String icon;

    public INFO_APP(String SERI_PHONE, String APPNAME, String pNAME, String verNAME, int verCODE, String icon) {
        this.SERI_PHONE = SERI_PHONE;
        this.APPNAME = APPNAME;
        this.pNAME = pNAME;
        this.verNAME = verNAME;
        this.verCODE = verCODE;
        this.icon = icon;
    }

    public INFO_APP() {

    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }

    public String getAPPNAME() {
        return APPNAME;
    }

    public void setAPPNAME(String APPNAME) {
        this.APPNAME = APPNAME;
    }

    public String getpNAME() {
        return pNAME;
    }

    public void setpNAME(String pNAME) {
        this.pNAME = pNAME;
    }

    public String getVerNAME() {
        return verNAME;
    }

    public void setVerNAME(String verNAME) {
        this.verNAME = verNAME;
    }

    public int getVerCODE() {
        return verCODE;
    }

    public void setVerCODE(int verCODE) {
        this.verCODE = verCODE;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
