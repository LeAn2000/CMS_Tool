package com.ChildMonitoringSystem.CMS.database;

import android.content.Context;

import com.ChildMonitoringSystem.CMS.Model.LocationLocalTrack;
import com.ChildMonitoringSystem.CMS.Value.Constant;
import com.ChildMonitoringSystem.CMS.Value.DateMethod;

import java.util.ArrayList;
import java.util.List;

public class Loc_DB_func {
    public static void insertLoc(String latitude, String longtitude, Context ctx){
        LocationLocalTrack local = new LocationLocalTrack(DateMethod.formatCurrenDateyyyy(),longtitude,latitude,Constant.Serial(ctx));
        LocationDB.getInstance(ctx).locationDAO().Insert(local);
    }

    public static void deleteAll(Context ctx)
    {
        LocationDB.getInstance(ctx).locationDAO().deleteAll();
    }
    public static List<LocationLocalTrack> getListLocal(Context ctx)
    {
        return LocationDB.getInstance(ctx).locationDAO().getListlocation();
    }


}
