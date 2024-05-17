package com.example.przeterminarz.model.database

import android.content.Context
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.przeterminarz.model.Product
import java.time.LocalDate

@Entity(tableName = "product")
data class ProductEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @ColumnInfo(name ="icon_name")
    val iconName: String,
    val name: String,
    val quantity: Int,
    @ColumnInfo(name ="expired_date")
    val expiredDate: LocalDate,
    val category: String,
    var ejected: Boolean,
) {
    fun toProduct(context: Context) :Product{
        val assetManager = context.assets
        val iconBitmap = BitmapFactory.decodeStream(assetManager.open("images/$iconName"))
        return Product(
            id= id,
            icon = iconBitmap,
            iconName = iconName,
            name =  name,
            quantity =  quantity,
            expiredDate =  expiredDate,
            category = category,
            ejected = ejected
        )
    }
    companion object{
        fun Product.toEntity():ProductEntity {
            return ProductEntity(
                id=id,
                iconName = iconName,
                name = name,
                quantity = quantity,
                expiredDate = expiredDate,
                category = category,
                ejected = ejected
            )
        }
    }
}