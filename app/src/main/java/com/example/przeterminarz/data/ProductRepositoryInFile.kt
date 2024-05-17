package com.example.przeterminarz.data

import android.content.Context
import com.example.przeterminarz.data.database.ProductDb
import com.example.przeterminarz.model.Product
import com.example.przeterminarz.model.database.ProductEntity.Companion.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryInFile(val context: Context) : ProductRepository {
    val db: ProductDb = ProductDb.open(context)
    override suspend fun getProductList(): List<Product> = withContext(Dispatchers.IO) {
        db.product.getAll().map { it.toProduct(context) }.sortedBy { it.expiredDate }
    }

    override suspend fun addProduct(product: Product) = withContext(Dispatchers.IO) {
        db.product.createOrUpdate(product.toEntity())
    }

    override suspend fun getProductById(id: Int): Product = withContext(Dispatchers.IO) {
        db.product.getById(id.toLong()).toProduct(context)
    }

    override suspend fun set(product: Product) = withContext(Dispatchers.IO){
        db.product.createOrUpdate(product.toEntity())
    }

    override suspend fun remove(id: Int) = withContext(Dispatchers.IO) {
        db.product.remove(id.toLong())
    }


}