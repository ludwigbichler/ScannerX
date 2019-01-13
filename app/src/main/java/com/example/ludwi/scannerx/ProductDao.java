package com.example.ludwi.scannerx;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product ORDER BY name ASC")
    public List<Product> getAllProducts();


}
