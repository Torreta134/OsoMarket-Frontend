package com.tuempresa.osornomarket.core.repository

import com.tuempresa.osornomarket.core.model.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun addProduct(product: Product): Boolean
    suspend fun updateProduct(product: Product): Boolean
    suspend fun deleteProduct(productId: Int): Boolean
}
