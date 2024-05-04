package com.example.przeterminarz.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.przeterminarz.R
import com.example.przeterminarz.adapter.ProductListAdapter
import com.example.przeterminarz.data.ProductRepository
import com.example.przeterminarz.data.ProductRepositoryImpl
import com.example.przeterminarz.data.RepositoryLocator
import com.example.przeterminarz.databinding.ActivityMainBinding
import com.example.przeterminarz.databinding.FragmentListBinding
import com.example.przeterminarz.model.FormType
import java.time.LocalDate


class ListFragment : Fragment() {
    private lateinit var binding : FragmentListBinding
    lateinit var productRepository : ProductRepository
    lateinit var productListAdapter : ProductListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productRepository = RepositoryLocator.productRepository

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(layoutInflater, container, false)
            .also {
                binding = it
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        productListAdapter = ProductListAdapter( onItemClick = {
            if (productRepository.getProductById(it).expiredDate.isBefore(LocalDate.now())) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Produkt przeterminowany")
                    .setMessage("Nie można edytować przeterminowanych produktów.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                findNavController().navigate(
                    R.id.action_listFragment_to_formFragment,
                    bundleOf("type" to FormType.Edit(it))
                )
            }
        }, onItemLongClick = {
            productRepository.remove(it)
        })
        binding.productList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productListAdapter
        }

        productListAdapter.productList = productRepository.getProductList()


        binding.addbutton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_formFragment,
                bundleOf("type" to FormType.New)
            )
        }
    }

    fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destination.id == R.id.listFragment) {
            productListAdapter.productList = productRepository.getProductList()
        }
    }

    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(::onDestinationChanged)
    }
    override fun onStop() {
        findNavController().removeOnDestinationChangedListener(::onDestinationChanged)
        super.onStop()
    }

}