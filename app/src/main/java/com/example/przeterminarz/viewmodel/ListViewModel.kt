package com.example.przeterminarz.viewmodel

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.przeterminarz.R
import com.example.przeterminarz.data.ProductRepository
import com.example.przeterminarz.data.RepositoryLocator
import com.example.przeterminarz.fragments.FormFragment
import com.example.przeterminarz.model.Product
import com.example.przeterminarz.model.navigation.AddProduct
import com.example.przeterminarz.model.navigation.Destination
import com.example.przeterminarz.model.navigation.EditProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ListViewModel : ViewModel() {

    private val productRepository : ProductRepository = RepositoryLocator.productRepository
    val products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val navigation = MutableLiveData<Destination>()
    val expiredProductEvent = MutableLiveData<Boolean>()


    fun filterProducts(category: String?, isExpired: Boolean?) {
        viewModelScope.launch(Dispatchers.IO) {
            val allProducts = productRepository.getProductList()
            val filtered = allProducts.filter {
                (category == FormFragment.CategoryData.emptyCategory || category == null || it.category == category) &&
                        (isExpired == null || (isExpired && it.expiredDate.isBefore(LocalDate.now())) ||
                                (!isExpired && it.expiredDate.isAfter(LocalDate.now())))
            }
            withContext(Dispatchers.Main) {
                products.value = filtered
            }
        }
    }
    init {
        loadProducts()
    }
    fun onAddProduct() {
        navigation.value = AddProduct()
    }
    fun onProductRemove(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            productRepository.remove(id)
            loadProducts()
        }
    }

    fun onProductEdit(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            val product = productRepository.getProductById(id)
            if (product.expiredDate.isBefore(LocalDate.now())) {
                expiredProductEvent.value = true
            } else {
                navigation.value = EditProduct(id)
            }
        }
    }

    fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destination.id == R.id.listFragment) {
            loadProducts()
        }
    }

    public fun loadProducts() {
        viewModelScope.launch(Dispatchers.Main){
            products.value = productRepository.getProductList()
        }
    }
}