package com.du.de.rasandummy.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void productInsertion(Product product);

    @Query("Select * from Product")
    List<Product> getProduct();

    @Query("Update Product set name = :etPdtName where id = :etPdtId")
    void updateProduct(String etPdtName, int etPdtId);

    @Query("Delete from Product where id = :etId")
    void productDeletion(int etId);
}
