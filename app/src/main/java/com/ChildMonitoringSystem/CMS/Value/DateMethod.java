package com.ChildMonitoringSystem.CMS.Value;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateMethod {
    public static String formatDate(long date, String pattern) {
        Date d = new Date(date);
        java.text.DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(d);
    }

    public static String formatCurrenDateyyyy() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.pattern);
        return simpleDateFormat.format(calendar.getTime());
    }
    public static String formatCurrenDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.pattern1);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String formatCurrenDateFirbase() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.patternfirebase);
        return simpleDateFormat.format(calendar.getTime());
    }

}
