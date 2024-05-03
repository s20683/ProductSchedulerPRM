package com.example.przeterminarz.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.przeterminarz.databinding.ItemProductBinding
import com.example.przeterminarz.model.Product
import java.time.LocalDate

class ProductItem(val itemViewBinding: ItemProductBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {
    fun onBind(productItem: Product) = with(itemViewBinding) {
        name.setText(productItem.name)
        quantity.setText(productItem.quantity.toString())
        expDate.setText(productItem.expiredDate.toString())
        if (productItem.expiredDate.isBefore(LocalDate.now())) {
            expired.setText("Yes")
        }
        else {
            expired.setText("No")
        }
        category.setText(productItem.category)
        if (productItem.ejected)
            ejected.setText("Yes")
        else
            ejected.setText("No")
        image.setImageResource(productItem.icon)
    }
}

class ProductListAdapter : RecyclerView.Adapter<ProductItem>() {
    public var productList: List<Product> = emptyList()
        set (value){
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductItem(binding);
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductItem, position: Int) {
        holder.onBind(productList[position])
    }
}