package com.example.przeterminarz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.przeterminarz.adapter.ProductListAdapter
import com.example.przeterminarz.data.ProductRepository
import com.example.przeterminarz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var productRepository : ProductRepository
    lateinit var bindining: ActivityMainBinding
    lateinit var productListAdapter : ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindining = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindining.root)
        productListAdapter = ProductListAdapter()
        bindining.productList.apply {

            adapter = productListAdapter
        }
        ViewCompat.setOnApplyWindowInsetsListener(bindining.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}