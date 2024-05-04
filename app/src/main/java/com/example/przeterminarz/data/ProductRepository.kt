package com.example.przeterminarz.data

import com.example.przeterminarz.model.Product

interface ProductRepository {
    fun getProductList(): List<Product>
    fun addProduct(product: Product)
    fun getProductById(id: Int): Product
    fun set(id: Int, product: Product)
    fun remove(it: Int): Boolean
}