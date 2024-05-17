package com.example.przeterminarz.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.przeterminarz.R
import com.example.przeterminarz.data.ProductRepository
import com.example.przeterminarz.data.RepositoryLocator
import com.example.przeterminarz.model.Product
import com.example.przeterminarz.model.navigation.Destination
import com.example.przeterminarz.model.navigation.PopBack
import java.time.LocalDate
import java.time.format.DateTimeParseException

class FormViewModel : ViewModel() {

    private val productRepository: ProductRepository = RepositoryLocator.productRepository
    private var edited: Product? = null

    val buttonText = MutableLiveData<Int>()
    val navigation = MutableLiveData<Destination>()

    val name = MutableLiveData("")
    val category = MutableLiveData("")
    val date = MutableLiveData("")
    val checked = MutableLiveData(false)
    val qty = MutableLiveData<String>("")

    fun init(id: Int?){
        edited = id?.let{
            productRepository.getProductById(id)
        }?.also {
            name.value = it.name
            category.value = it.category
            date.value = it.expiredDate.toString()
            checked.value = it.ejected
            qty.value = it.quantity.toString()
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
                id = productRepository.getNextId(),
                R.drawable.brokul,
                name,
                quantity,
                it,
                category,
                checked
            )

            if (edited == null) {
                productRepository.addProduct(product)
            } else {
                productRepository.set(product.id, product)
            }
        } ?: run {

        }
        navigation.value = PopBack()
    }
}