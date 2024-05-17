package com.example.przeterminarz.data

import com.example.przeterminarz.model.Product

interface ProductRepository {
    suspend fun getProductList(): List<Product>
    suspend fun addProduct(product: Product)
    suspend fun getProductById(id: Int): Product
    suspend fun set( product: Product)
    suspend fun remove(id: Int)

    companion object {
        const val GENERATE_ID = 0;
    }
}