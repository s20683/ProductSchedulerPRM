package com.example.przeterminarz.model

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import java.time.LocalDate

data class Product(
    val id:Int,
    val icon: Bitmap,
    val iconName: String,
    val name: String,
    val quantity: Int,
    val expiredDate: LocalDate,
    val category: String,
    var ejected: Boolean
)
