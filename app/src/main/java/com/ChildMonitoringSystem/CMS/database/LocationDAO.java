package com.ChildMonitoringSystem.CMS.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ChildMonitoringSystem.CMS.Model.LocationLocalTrack;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface LocationDAO {

    @Insert
    void Insert(LocationLocalTrack local);

    @Query("select * from local_location")
    List<LocationLocalTrack> getListlocation();

    @Query("delete from local_location")
    void deleteAll();
}
