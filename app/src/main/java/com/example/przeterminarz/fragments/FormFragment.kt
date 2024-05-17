package com.example.przeterminarz.fragments

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.przeterminarz.R
import com.example.przeterminarz.data.ProductRepository
import com.example.przeterminarz.data.RepositoryLocator
import com.example.przeterminarz.databinding.FragmentFormBinding
import com.example.przeterminarz.model.FormType
import com.example.przeterminarz.model.Product
import com.example.przeterminarz.viewmodel.FormViewModel
import java.time.LocalDate

private const val TYPE_KEY = "type"

class FormFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var type: FormType
    private lateinit var binding: FragmentFormBinding
    private var date: LocalDate = LocalDate.now()
    private var category: String = ""
    private var checked: Boolean = false
    private val categories = arrayOf("Produkty Spożywcze", "Kosmetyki", "Leki")
    private lateinit var categoryAdapter : ArrayAdapter<String>

    private val viewModel by viewModels<FormViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                it.viewModel = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            viewModel.init((type as? FormType.Edit)?.id)
            viewModel.navigation.observe(viewLifecycleOwner) {
                it.resolve(findNavController())
            }
        }

//        updateButtonBasedOnType()
        binding.labelName.text = getString(R.string.product_name)
        binding.fieldName.hint = getString(R.string.product_name2)
        binding.labelCategory.text = getString(R.string.product_category)
        binding.labelQuantity.text = getString(R.string.product_qty)
//        binding.labelEjected.text = getString(R.string.product_ejected)
        binding.labelDate.text = getString(R.string.product_exp_date)

        categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.fieldCategory.adapter = categoryAdapter

        binding.buttonDate.text = getString(R.string.select_date)
        binding.buttonDate.setOnClickListener {
            showDatePicker()
        }


        binding.fieldCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    category = parent.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

//        binding.fieldDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            date = LocalDate.of(year, month + 1, dayOfMonth)
//        }


//        binding.switchEjected.setOnCheckedChangeListener { _, isChecked ->
//            checked = isChecked
//        }

//        (type as? FormType.Edit)?.let {
//            edited = productRepository.getProductById(it.id)
//                .also {
//                    with(binding.fieldName){
//                        setText(it.name)
//                    }
//                    with(binding.fieldQuantity) {
//                        setText(it.quantity.toString())
//                    }
//                    with(binding.fieldCategory) {
//                        val categoryPosition = categoryAdapter.getPosition(it.category)
//                        setSelection(categoryPosition)
//                    }
////                    with (binding.switchEjected) {
////                        isChecked = it.ejected
////                    }
//                    with (binding.buttonDate) {
//                        date = it.expiredDate
//                        binding.buttonDate.text = date.toString()
//                    }
//                }
//
//        }
    }
//    private fun updateButtonBasedOnType() {
//        binding.button.text = when (type) {
//            is FormType.Edit -> getString(R.string.save)
//            FormType.New -> getString(R.string.add)
//        }
//        binding.button.setOnClickListener {
//            saveProduct((type as? FormType.Edit)?.id)
//            findNavController().popBackStack()
//        }
//    }

    private fun showDatePicker() {
        val today = LocalDate.now()
        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                if (selectedDate.isBefore(today)) {
                    Toast.makeText(context, "Past dates are not allowed.", Toast.LENGTH_SHORT).show()
                    // Optionally reset the DatePicker to today's date or reopen the dialog
                } else {
                    date = selectedDate
                    binding.buttonDate.text = date.toString()
                    // Update your UI accordingly
                }
            }, today.year, today.monthValue - 1, today.dayOfMonth
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000 // Disables past dates
        datePickerDialog.show()
    }

}


