package com.pr.productkereview.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pr.productkereview.db.entity.Products;

import java.util.List;

@Dao
public interface ProductDAO {
    @Insert
    public long addProducts(Products products);

    @Update
    public void updateProducts(Products products);

    @Delete
    public void deleteProducts(Products products);

    @Query("select * from products")
    public List<Products> getProducts();

    @Query("select * from products where id ==:productId")
    public Products getProduct(long productId);
}
