package com.hamedmajdi.drugreminder;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DrugDataClass.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DrugDao drugDao();
}