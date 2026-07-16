package com.tuempresa.osornomarket.core.model

import kotlinx.serialization.Serializable

/**
 * Representa un Teléfono o Producto en el Marketplace.
 */
@Serializable
data class Product(
    val id: Int = 0,
    val name: String = "",         // Ej: iPhone 13
    val brand: String = "",        // Ej: Apple
    val description: String = "",
    val price: Long = 0L,          // Actualizado a Long para que coincida con Backend
    val type: String = "",         // Ej: Smartphone, Laptop
    val sellerId: String = "",
    val imageUrl: String = "",
    val condition: String = "Nuevo" // Nuevo, Usado, Reacondicionado
)
