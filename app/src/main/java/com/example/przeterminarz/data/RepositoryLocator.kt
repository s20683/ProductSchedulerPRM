package com.example.przeterminarz.data

import android.content.Context

object RepositoryLocator {
    lateinit var productRepository : ProductRepository
        private set
    fun init(context: Context) {
        productRepository = ProductRepositoryInFile(context)
    }
}