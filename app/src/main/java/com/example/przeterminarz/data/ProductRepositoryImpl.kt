package com.example.przeterminarz.data

import com.example.przeterminarz.R
import com.example.przeterminarz.model.Product
import java.time.LocalDate

object ProductRepositoryImpl : ProductRepository{
    private val productList = mutableListOf<Product>(
        Product(R.drawable.apap, "Apap", 2, LocalDate.now().minusDays(1),"Meds", false),
        Product(R.drawable.brokul, "Brokuł", 3, LocalDate.now().plusDays(1),"Vegetables", false),
    )
    override fun getProductList(): List<Product> = productList
}