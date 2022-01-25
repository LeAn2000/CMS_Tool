package com.ChildMonitoringSystem.CMS.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ChildMonitoringSystem.CMS.Model.LocationLocalTrack;

@Database(entities = {LocationLocalTrack.class},version = 1,exportSchema = false)
public abstract class LocationDB extends RoomDatabase {
    private static final String DATABASENAME = "localtion.db";
    private static LocationDB instance;

    public static synchronized LocationDB getInstance(Context ctx)
    {
        if(instance==null)
        {
            instance = Room.databaseBuilder(ctx.getApplicationContext(), LocationDB.class, DATABASENAME)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract LocationDAO locationDAO();

}
