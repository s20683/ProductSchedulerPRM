package com.example.przeterminarz.data

import com.example.przeterminarz.R
import com.example.przeterminarz.model.Product
import java.time.LocalDate

object ProductRepositoryImpl : ProductRepository{
    private val productList = mutableListOf<Product>(
//        Product(41, R.drawable.apap, "Apap", 2, LocalDate.now().minusDays(1),"Kosmetyki", false),
//        Product(32, R.drawable.brokul, "Brokuł", 3, LocalDate.now().plusDays(1),"Leki", false),
    )
    override suspend fun getProductList(): List<Product> = productList
    override suspend fun addProduct(product: Product) {
        productList.add(if (product.id == 0)  product.copy(id = getNextId()) else product)
    }

    override suspend fun getProductById(id: Int): Product =
        productList.first{it.id == id}
    override suspend fun set(product: Product) {
        val index = productList.indexOfFirst { it.id == product.id }
        productList[index] = product
    }

    override suspend fun remove(id: Int) {
        productList.removeIf {it.id == id}
    }

    private fun getNextId(): Int {
        return productList.maxOf { it.id }.inc()
    }
}