package com.example.perfumestore

import androidx.lifecycle.ViewModel
import com.example.perfumestore.data.model.ProductItem

class MainViewModel : ViewModel() {
    var currentProduct: ProductItem? = null
}