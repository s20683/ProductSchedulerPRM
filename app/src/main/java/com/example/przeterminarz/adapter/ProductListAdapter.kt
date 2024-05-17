package com.example.przeterminarz.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.przeterminarz.R
import com.example.przeterminarz.databinding.ItemProductBinding
import com.example.przeterminarz.model.Product
import java.time.LocalDate

class ProductItem(private val itemViewBinding: ItemProductBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {
    var id: Int = 0
        private set
    fun onBind(productItem: Product, onItemClick: (Int) -> Unit, onItemLongClick: () -> Boolean) = with(itemViewBinding) {
        val context = itemView.context
        id = productItem.id

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
        image.setImageBitmap(productItem.icon)

        root.setOnClickListener{
            onItemClick(productItem.id)
        }
        root.setOnLongClickListener{
            onItemLongClick()
        }
    }
}

class ProductListAdapter(private val onItemClick: (Int) -> Unit, private val onItemLongClick: (Int) -> Unit) : RecyclerView.Adapter<ProductItem>() {
    public var productList: List<Product> = emptyList()
        set (value){
            field = value
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductItem(binding);
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductItem, position: Int) {
        holder.onBind(productList[position], onItemClick = {
            onItemClick(it)
        }, onItemLongClick = {
            showDeletionDialog(holder.itemView.context, productList[position], position)
            true
        })
    }
    private fun showDeletionDialog(context: Context, product: Product, position: Int) {
        if (product.expiredDate.isBefore(LocalDate.now())) {
            AlertDialog.Builder(context)
                .setTitle("Produkt przeterminowany")
                .setMessage("Czy chcesz oznaczyć ten produkt jako wyrzucony?")
                .setPositiveButton("Tak") { dialog, _ ->
                    product.ejected = true
                    notifyItemChanged(position)
                    dialog.dismiss()
                }
                .setNegativeButton("Nie") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        } else {
            AlertDialog.Builder(context)
                .setTitle("Usunąć produkt?")
                .setMessage("Czy chcesz usunąć ten produkt z listy?")
                .setPositiveButton("Usuń") { dialog, _ ->
                    onItemLongClick(product.id)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, productList.size)
                    dialog.dismiss()
                }
                .setNegativeButton("Anuluj") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
    }
}