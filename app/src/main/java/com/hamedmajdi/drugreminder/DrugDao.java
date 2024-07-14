package com.hamedmajdi.drugreminder;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DrugDao {
    @Insert
    void insert(DrugDataClass drugDataClass);

    @Query("SELECT * FROM drugs_table")
    List<DrugDataClass> getAllDrugs();
}
