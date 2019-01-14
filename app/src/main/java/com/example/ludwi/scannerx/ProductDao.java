package com.example.ludwi.scannerx;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product ORDER BY bezeichnung ASC")
    public List<Product> getAllProducts();

    @Insert
    public void insertProduct(Product product);


}
