package com.ChildMonitoringSystem.CMS.Model;

public class ACCOUNT {
    private String PHONE_NUMBERS;
    private String PASSWORD_USERS;
    private String NAME;
    private String DATE_OF_BIRTH;
    private String EMAIL;
    private boolean PERMISSION;
    private String TOTAL_PHONE_MONITOR;

    public ACCOUNT(String PHONE_NUMBERS, String PASSWORD_USERS, String NAME, String DATE_OF_BIRTH, String EMAIL, boolean PERMISSION, String TOTAL_PHONE_MONITOR) {
        this.PHONE_NUMBERS = PHONE_NUMBERS;
        this.PASSWORD_USERS = PASSWORD_USERS;
        this.NAME = NAME;
        this.DATE_OF_BIRTH = DATE_OF_BIRTH;
        this.EMAIL = EMAIL;
        this.PERMISSION = PERMISSION;
        this.TOTAL_PHONE_MONITOR = TOTAL_PHONE_MONITOR;
    }

    public String getPHONE_NUMBERS() {
        return PHONE_NUMBERS;
    }

    public void setPHONE_NUMBERS(String PHONE_NUMBERS) {
        this.PHONE_NUMBERS = PHONE_NUMBERS;
    }

    public String getPASSWORD_USERS() {
        return PASSWORD_USERS;
    }

    public void setPASSWORD_USERS(String PASSWORD_USERS) {
        this.PASSWORD_USERS = PASSWORD_USERS;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDATE_OF_BIRTH() {
        return DATE_OF_BIRTH;
    }

    public void setDATE_OF_BIRTH(String DATE_OF_BIRTH) {
        this.DATE_OF_BIRTH = DATE_OF_BIRTH;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public boolean isPERMISSION() {
        return PERMISSION;
    }

    public void setPERMISSION(boolean PERMISSION) {
        this.PERMISSION = PERMISSION;
    }

    public String getTOTAL_PHONE_MONITOR() {
        return TOTAL_PHONE_MONITOR;
    }

    public void setTOTAL_PHONE_MONITOR(String TOTAL_PHONE_MONITOR) {
        this.TOTAL_PHONE_MONITOR = TOTAL_PHONE_MONITOR;
    }
}
