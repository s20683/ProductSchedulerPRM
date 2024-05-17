package com.example.przeterminarz.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.przeterminarz.model.database.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(product: ProductEntity)

    @Query("SELECT * FROM product;")
    suspend fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE id = :id;")
    suspend fun getById(id: Long) : ProductEntity

    @Query("DELETE FROM product WHERE id = :productId;")
    suspend fun remove(productId: Long)
}