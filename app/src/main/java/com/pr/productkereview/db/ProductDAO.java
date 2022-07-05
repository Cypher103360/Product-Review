package com.pr.productkereview.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pr.productkereview.db.entity.Products;

import java.util.List;

@Dao
public interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addProducts(Products products);

    @Update
    public void updateProducts(Products products);

    @Delete
    public void deleteProducts(Products products);

    @Query("DELETE FROM products where id NOT IN (SELECT id from products ORDER BY id DESC LIMIT 3)")
    public void deleteItemsByLimit();

    @Query("DELETE FROM products where id ==:productId")
    public void deleteDuplicateItems(int productId);

    @Query("select * from products")
    public List<Products> getProducts();

    @Query("select * from products where id ==:productId")
     Products getProduct(long productId);

    @Query("SELECT EXISTS(select * from products where productTitle ==:title)")
     boolean getProductByTitle(String title);


}
