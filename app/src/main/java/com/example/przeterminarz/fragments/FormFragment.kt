package com.example.przeterminarz.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.example.przeterminarz.R
import com.example.przeterminarz.data.ProductRepository
import com.example.przeterminarz.data.RepositoryLocator
import com.example.przeterminarz.databinding.FragmentFormBinding
import com.example.przeterminarz.model.FormType
import com.example.przeterminarz.model.Product
import java.time.LocalDate

private const val TYPE_KEY = "type"

class FormFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var type: FormType
    private lateinit var binding: FragmentFormBinding
    private lateinit var productRepository: ProductRepository
    private var date: LocalDate = LocalDate.now()
    private var category: String = ""
    private var checked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productRepository = RepositoryLocator.productRepository
        arguments?.let {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(TYPE_KEY, FormType::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getSerializable(TYPE_KEY) as? FormType
            } ?: FormType.New
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFormBinding.inflate(layoutInflater, container, false)
            .also {
                binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateButtonBasedOnType()

        binding.fieldCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    category = parent.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.fieldDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = LocalDate.of(year, month + 1, dayOfMonth)
        }

        binding.switchEjected.setOnCheckedChangeListener { _, isChecked ->
            checked = isChecked
        }
    }
    private fun updateButtonBasedOnType() {
        binding.button.text = when (type) {
            is FormType.Edit -> getString(R.string.save)
            FormType.New -> getString(R.string.add)
        }
        binding.button.setOnClickListener {
            saveProduct()
            findNavController().popBackStack()
        }
    }



    private fun saveProduct() {
        val quantityString = binding.fieldQuantity.text.toString()
        val quantity = if (quantityString.isNotEmpty()) quantityString.toInt() else 0

        val product = Product(
            R.drawable.brokul,
            binding.fieldName.text.toString(),
            quantity,
            date,
            category,
            checked
        )
        productRepository.addProduct(product)
    }

}


