package com.example.przeterminarz.data

import com.example.przeterminarz.R
import com.example.przeterminarz.model.Product
import java.time.LocalDate

object ProductRepositoryImpl : ProductRepository{
    private val productList = mutableListOf<Product>(
        Product(41, R.drawable.apap, "Apap", 2, LocalDate.now().minusDays(1),"Kosmetyki", false),
        Product(32, R.drawable.brokul, "Brokuł", 3, LocalDate.now().plusDays(1),"Leki", false),
    )
    override fun getProductList(): List<Product> = productList
    override fun addProduct(product: Product) {
        productList.add(product)
    }

    override fun getProductById(id: Int): Product =
        productList.first{it.id == id}
    override fun set(id: Int, product: Product) {
        val index = productList.indexOfFirst { it.id == id }
        productList[index] = product
    }

    override fun remove(id: Int): Boolean {
        return productList.removeIf {it.id == id}
    }

    override fun getNextId(): Int {
        return productList.maxOf { it.id }.inc()
    }
}