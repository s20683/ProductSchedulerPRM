package com.example.przeterminarz.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.przeterminarz.R
import com.example.przeterminarz.databinding.ItemProductBinding
import com.example.przeterminarz.model.Product
import java.time.LocalDate
import kotlin.coroutines.coroutineContext

class ProductItem(private val itemViewBinding: ItemProductBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {
    fun onBind(productItem: Product, onItemClick: () -> Unit) = with(itemViewBinding) {
        val context = itemView.context

        name.setText(productItem.name)
        quantity.setText(productItem.quantity.toString())
        mainExpDateLabel.text = context.getString(R.string.product_exp_date)
        mainCategoryLabel.text = context.getString(R.string.product_category)
        mainEjectedLabel.text = context.getString(R.string.product_ejected)
        expDate.setText(productItem.expiredDate.toString())
        if (productItem.expiredDate.isBefore(LocalDate.now())) {
            expiredButton.isChecked = false
        }
        else {
            expiredButton.isChecked = true
        }

        category.setText(productItem.category)
        if (productItem.ejected)
            ejected.setText("Yes")
        else
            ejected.setText("No")
        image.setImageResource(productItem.icon)

        root.setOnClickListener{
            onItemClick()
        }
    }
}

class ProductListAdapter(private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<ProductItem>() {
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
        holder.onBind(productList[position]) {
            onItemClick(position)
        }
    }
}