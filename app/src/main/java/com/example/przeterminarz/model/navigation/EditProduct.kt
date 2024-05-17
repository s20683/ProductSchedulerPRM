package com.example.przeterminarz.model.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.przeterminarz.R
import com.example.przeterminarz.model.FormType

class EditProduct(val id: Int) : Destination(){
    override fun navigate(controller: NavController) {
        controller.navigate(
            R.id.action_listFragment_to_formFragment,
            bundleOf("type" to FormType.Edit(id))
        )
    }
}