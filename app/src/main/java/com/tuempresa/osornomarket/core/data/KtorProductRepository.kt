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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

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

    // Cambia la IP si usas dispositivo físico en vez del emulador (ej: 192.168.x.x)
    private val baseUrl = "http://10.0.2.2:8080/products"

    override suspend fun getAllProducts(): List<Product> {
        return try {
            client.get(baseUrl).body()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun addProduct(product: Product): Boolean {
        return try {
            val response = client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(product)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun updateProduct(product: Product): Boolean {
        return try {
            val response = client.put("$baseUrl/${product.id}") {
                contentType(ContentType.Application.Json)
                setBody(product)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteProduct(productId: Int): Boolean {
        return try {
            val response = client.delete("$baseUrl/$productId")
            response.status.isSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
