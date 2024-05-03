package com.example.przeterminarz.data

import com.example.przeterminarz.model.Product

interface ProductRepository {
    fun getProductList(): List<Product>
}