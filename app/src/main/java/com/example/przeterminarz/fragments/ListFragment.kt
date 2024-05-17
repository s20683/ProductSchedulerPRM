package com.example.przeterminarz.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.przeterminarz.R
import com.example.przeterminarz.adapter.ProductListAdapter
import com.example.przeterminarz.databinding.FragmentListBinding
import com.example.przeterminarz.viewmodel.ListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListFragment : Fragment() {
    private lateinit var binding : FragmentListBinding
    private lateinit var productListAdapter : ProductListAdapter
    private val viewModel : ListViewModel by viewModels()
    private lateinit var filterButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(layoutInflater, container, false)
            .also {
                binding = it
                it.viewModel = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadProducts()
        filterButton = view.findViewById(R.id.filterButton)
        viewModel.expiredProductEvent.observe(viewLifecycleOwner) { isExpired ->
            if (isExpired) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Produkt przeterminowany")
                    .setMessage("Nie można edytować przeterminowanych produktów.")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        viewModel.expiredProductEvent.value = false
                    }
                    .show()
            }
        }

        productListAdapter = ProductListAdapter( onItemClick = {
             viewModel.onProductEdit(it)
//            if (productRepository.getProductById(it).expiredDate.isBefore(LocalDate.now())) {
//                AlertDialog.Builder(requireContext())
//                    .setTitle("Produkt przeterminowany")
//                    .setMessage("Nie można edytować przeterminowanych produktów.")
//                    .setPositiveButton("OK", null)
//                    .show()
//            } else {
//                findNavController().navigate(
//                    R.id.action_listFragment_to_formFragment,
//                    bundleOf("type" to FormType.Edit(it))
//                )
//            }
        }, onItemLongClick = {
            viewModel.onProductRemove(it);
        })
        binding.productList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productListAdapter
        }

        viewModel.products.observe(viewLifecycleOwner){
            productListAdapter.productList = it
            productListAdapter.notifyDataSetChanged()
        }
        viewModel.navigation.observe(viewLifecycleOwner) {
            it.resolve(findNavController())
        }

        filterButton.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter, null)
        val spinnerCategories = dialogView.findViewById<Spinner>(R.id.spinner_categories)
        val radioGroupStatus = dialogView.findViewById<RadioGroup>(R.id.radio_group_status)

        val categories = arrayOf(FormFragment.CategoryData.emptyCategory) + FormFragment.CategoryData.categories
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategories.adapter = adapter

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        builder.setTitle("Opcje filtracji")
        builder.setPositiveButton("Filtruj") { dialog, _ ->
            val selectedCategory = spinnerCategories.selectedItem.toString()
            val isExpired = when (radioGroupStatus.checkedRadioButtonId) {
                R.id.radio_valid -> false
                R.id.radio_expired -> true
                else -> null
            }
            viewModel.filterProducts(selectedCategory, isExpired)
        }
        builder.setNegativeButton("Anuluj") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }



    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(viewModel::onDestinationChanged)
    }
    override fun onStop() {
        findNavController().removeOnDestinationChangedListener(viewModel::onDestinationChanged)
        super.onStop()
    }

}