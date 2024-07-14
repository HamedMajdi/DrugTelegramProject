package com.hamedmajdi.drugreminder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "drugs_table")
public class DrugDataClass {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String drugName;
    public String drugTime;
    public int quantity;

    // Add a constructor that matches the parameters you want to pass
    public DrugDataClass(String drugName, String drugTime, int quantity) {
        this.drugName = drugName;
        this.drugTime = drugTime;
        this.quantity = quantity;
    }
}