package com.tuempresa.osornomarket.core.data

import com.tuempresa.osornomarket.core.model.Product
import com.tuempresa.osornomarket.core.repository.ProductRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import android.util.Log

/**
 * DTO que coincide exactamente con CreateProductRequest del backend.
 * No incluye id ni sellerId (el backend los asigna automáticamente).
 */
@Serializable
data class CreateProductRequest(
    val name: String,
    val brand: String,
    val description: String,
    val price: Long,
    val type: String,
    val imageUrl: String,
    val condition: String
)

class KtorProductRepository : ProductRepository {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    // Usa la IP de tu computador en la red Wi-Fi local
    private val baseUrl = "http://192.168.1.87:8080/products"

    override suspend fun getAllProducts(): List<Product> {
        return try {
            client.get(baseUrl).body()
        } catch (e: Exception) {
            Log.e("ProductRepo", "Error al obtener productos", e)
            emptyList()
        }
    }

    override suspend fun addProduct(product: Product): Boolean {
        return try {
            val request = CreateProductRequest(
                name = product.name,
                brand = product.brand,
                description = product.description,
                price = product.price,
                type = product.type,
                imageUrl = product.imageUrl,
                condition = product.condition
            )
            Log.d("ProductRepo", "Enviando producto: $request con token: ${TokenManager.token?.take(20)}...")
            val response = client.post(baseUrl) {
                header("Authorization", "Bearer ${TokenManager.token}")
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val body = response.bodyAsText()
            Log.d("ProductRepo", "Respuesta crear: HTTP ${response.status.value} - $body")
            response.status.isSuccess()
        } catch (e: Exception) {
            Log.e("ProductRepo", "Excepcion al agregar producto", e)
            false
        }
    }

    override suspend fun updateProduct(product: Product): Boolean {
        return try {
            val request = CreateProductRequest(
                name = product.name,
                brand = product.brand,
                description = product.description,
                price = product.price,
                type = product.type,
                imageUrl = product.imageUrl,
                condition = product.condition
            )
            val response = client.put("$baseUrl/${product.id}") {
                header("Authorization", "Bearer ${TokenManager.token}")
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val body = response.bodyAsText()
            Log.d("ProductRepo", "Respuesta actualizar: HTTP ${response.status.value} - $body")
            response.status.isSuccess()
        } catch (e: Exception) {
            Log.e("ProductRepo", "Excepcion al actualizar producto", e)
            false
        }
    }

    override suspend fun deleteProduct(productId: Int): Boolean {
        return try {
            val response = client.delete("$baseUrl/$productId") {
                header("Authorization", "Bearer ${TokenManager.token}")
            }
            val body = response.bodyAsText()
            Log.d("ProductRepo", "Respuesta eliminar: HTTP ${response.status.value} - $body")
            response.status.isSuccess()
        } catch (e: Exception) {
            Log.e("ProductRepo", "Excepcion al eliminar producto", e)
            false
        }
    }
}
