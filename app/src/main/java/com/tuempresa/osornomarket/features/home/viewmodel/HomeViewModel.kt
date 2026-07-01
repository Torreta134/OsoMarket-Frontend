package com.tuempresa.osornomarket.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuempresa.osornomarket.core.data.KtorProductRepository
import com.tuempresa.osornomarket.core.model.Product
import com.tuempresa.osornomarket.core.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ProductRepository = KtorProductRepository()
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val productList = repository.getAllProducts()
                
                // Buscamos si hay datos del mundial
                val containsOldData = productList.any { it.name.contains("Lámina") || it.description.contains("Mundial") }
                
                if (productList.isEmpty() || containsOldData) {
                    // Si hay datos viejos, primero borramos los que existen para que no se mezclen
                    if (containsOldData) {
                        productList.forEach { 
                            if (it.name.contains("Lámina") || it.description.contains("Mundial")) {
                                repository.deleteProduct(it.id)
                            }
                        }
                    }
                    seedDemoData()
                } else {
                    _products.value = productList
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al cargar productos", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun seedDemoData() {
        viewModelScope.launch {
            val demoProducts = listOf(
                Product(
                    name = "iPhone 15 Pro",
                    brand = "Apple",
                    description = "Titanio natural, chip A17 Pro, 128GB.",
                    price = 1200000,
                    imageUrl = "https://images.unsplash.com/photo-1696446701796-da61225697cc?q=80&w=500",
                    condition = "Nuevo"
                ),
                Product(
                    name = "Zapatillas Nike Air Max",
                    brand = "Nike",
                    description = "Amortiguación clásica y comodidad extrema para el día a día.",
                    price = 89990,
                    imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=500",
                    condition = "Nuevo"
                )
            )
            demoProducts.forEach { repository.addProduct(it) }
            // Volver a cargar después de insertar
            val updatedList = repository.getAllProducts()
            _products.value = updatedList
        }
    }
}
