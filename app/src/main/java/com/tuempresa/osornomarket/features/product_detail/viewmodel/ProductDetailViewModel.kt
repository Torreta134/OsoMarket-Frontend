package com.tuempresa.osornomarket.features.product_detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuempresa.osornomarket.core.data.KtorProductRepository
import com.tuempresa.osornomarket.core.model.Product
import com.tuempresa.osornomarket.core.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository = KtorProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailState>(ProductDetailState.Idle)
    val uiState: StateFlow<ProductDetailState> = _uiState

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            _uiState.value = ProductDetailState.Loading
            val success = if (product.id == 0) {
                repository.addProduct(product)
            } else {
                repository.updateProduct(product)
            }
            
            if (success) {
                _uiState.value = ProductDetailState.Success
            } else {
                _uiState.value = ProductDetailState.Error("No se pudo guardar el teléfono")
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailState.Loading
            if (repository.deleteProduct(productId)) {
                _uiState.value = ProductDetailState.Success
            } else {
                _uiState.value = ProductDetailState.Error("Error al eliminar")
            }
        }
    }
}

sealed class ProductDetailState {
    object Idle : ProductDetailState()
    object Loading : ProductDetailState()
    object Success : ProductDetailState()
    data class Error(val message: String) : ProductDetailState()
}
