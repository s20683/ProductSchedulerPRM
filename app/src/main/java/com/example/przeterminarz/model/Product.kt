package com.example.przeterminarz.model

import androidx.annotation.DrawableRes
import java.time.LocalDate

data class Product(
    val id:Int,
    @DrawableRes
    val icon: Int,
    val name: String,
    val quantity: Int,
    val expiredDate: LocalDate,
    val category: String,
    var ejected: Boolean
)
