package com.example.przeterminarz.viewmodel

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.przeterminarz.R
import com.example.przeterminarz.data.ProductRepository
import com.example.przeterminarz.data.RepositoryLocator
import com.example.przeterminarz.model.Product
import com.example.przeterminarz.model.navigation.AddProduct
import com.example.przeterminarz.model.navigation.Destination
import com.example.przeterminarz.model.navigation.EditProduct
import java.time.LocalDate

class ListViewModel : ViewModel() {

    private val productRepository : ProductRepository = RepositoryLocator.productRepository
    val products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val navigation = MutableLiveData<Destination>()
    val expiredProductEvent = MutableLiveData<Boolean>()


    fun onAddProduct() {
        navigation.value = AddProduct()
    }
    fun onProductRemove(id: Int) {
        productRepository.remove(id)
        loadProducts()
    }

    fun onProductEdit(id: Int) {
        val product = productRepository.getProductById(id)
        if (product.expiredDate.isBefore(LocalDate.now())) {
            expiredProductEvent.value = true
        } else {
            navigation.value = EditProduct(id)
        }
    }

    fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destination.id == R.id.listFragment) {
            loadProducts()
        }
    }

    private fun loadProducts() {
        products.value = productRepository.getProductList()
    }
}