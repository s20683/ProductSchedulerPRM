package com.example.przeterminarz.fragments

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeLifecycleOwner

object BindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["categories", "category"], requireAll = true)
    fun Spinner.setCategories(categories: MutableLiveData<List<String>>, selectedCategory: MutableLiveData<String>) {
        val lifecycleOwner = findViewTreeLifecycleOwner()
        categories.observe(lifecycleOwner!!) { items ->
            val adapter =
                ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            this.adapter = adapter

            selectedCategory.value?.let { category ->
                val position = items.indexOf(category)
                if (position >= 0) {
                    this.setSelection(position)
                }
            }
        }

        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position) as String
                selectedCategory.value = item
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
}