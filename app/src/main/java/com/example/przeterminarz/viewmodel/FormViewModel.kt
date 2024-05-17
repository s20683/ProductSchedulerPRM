package com.example.przeterminarz.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.przeterminarz.R
import com.example.przeterminarz.data.ProductRepository
import com.example.przeterminarz.data.ProductRepository.Companion.GENERATE_ID
import com.example.przeterminarz.data.RepositoryLocator
import com.example.przeterminarz.model.Product
import com.example.przeterminarz.model.navigation.Destination
import com.example.przeterminarz.model.navigation.PopBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeParseException

class FormViewModel : ViewModel() {

    private val productRepository: ProductRepository = RepositoryLocator.productRepository
    private var edited: Product? = null
    val categories = MutableLiveData<List<String>>(listOf("Produkty Spożywcze", "Kosmetyki", "Leki"))

    val buttonText = MutableLiveData<Int>()
    val navigation = MutableLiveData<Destination>()

    val name = MutableLiveData("")
    val category = MutableLiveData("")
    val date = MutableLiveData("")
    val checked = MutableLiveData(false)
    val qty = MutableLiveData<String>("")

    fun init(id: Int?){
        id?.let{
            viewModelScope.launch {
                edited = productRepository.getProductById(id).also {
                    withContext(Dispatchers.Main) {
                        name.value = it.name
                        category.value = it.category
                        date.value = it.expiredDate.toString()
                        checked.value = it.ejected
                        qty.value = it.quantity.toString()
                    }
                }

            }
        }
        buttonText.value = when(edited) {
            null -> R.string.add
            else -> R.string.save
        }
    }


    fun onSave() {
        val quantityString = qty.value.toString()
        val quantity = if (quantityString.isNotEmpty()) quantityString.toInt() else 0

        val name = name.value.orEmpty()
        val date = date.value.orEmpty()
        val category = category.value.orEmpty()
        val checked = checked.value ?: false

        val localDate: LocalDate? = try {
            LocalDate.parse(date)
        } catch (e: DateTimeParseException) {
            null
        }

        localDate?.let {
            val product = edited?.copy(
                name = name,
                quantity = quantity,
                expiredDate = it,
                category = category,
                ejected = checked
            ) ?: Product(
                id = GENERATE_ID,
                icon = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888),
                iconName = "brokul.jpg",
                name = name,
                quantity = quantity,
                expiredDate = it,
                category = category,
                ejected = checked
            )

            viewModelScope.launch {
                if (edited == null) {
                    productRepository.addProduct(product)
                } else {
                    productRepository.set(product)
                }
                withContext(Dispatchers.Main) {
                    navigation.value = PopBack()
                }
            }
        } ?: run {
            navigation.value = PopBack()
        }
    }
}