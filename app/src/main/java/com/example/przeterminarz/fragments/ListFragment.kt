package com.example.przeterminarz.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.przeterminarz.adapter.ProductListAdapter
import com.example.przeterminarz.databinding.FragmentListBinding
import com.example.przeterminarz.viewmodel.ListViewModel


class ListFragment : Fragment() {
    private lateinit var binding : FragmentListBinding
    private lateinit var productListAdapter : ProductListAdapter
    private val viewModel : ListViewModel by viewModels()
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
        }
        viewModel.navigation.observe(viewLifecycleOwner) {
            it.resolve(findNavController())
        }

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