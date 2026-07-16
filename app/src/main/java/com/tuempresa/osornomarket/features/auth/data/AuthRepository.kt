package com.tuempresa.osornomarket.features.auth.data

import com.tuempresa.osornomarket.core.data.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import android.util.Log
import io.ktor.client.statement.bodyAsText

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterRequest(val name: String, val email: String, val rut: String, val password: String)

@Serializable
data class LoginResponse(val token: String)

class AuthRepository {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
        }
    }
    private val baseUrl = "http://192.168.1.87:8080/auth"

    suspend fun login(req: LoginRequest): Result<Boolean> {
        return try {
            Log.d("AuthRepository", "Intentando login en $baseUrl/login con email=${req.email}")
            val res = client.post("$baseUrl/login") {
                contentType(ContentType.Application.Json)
                setBody(req)
            }
            val responseBody = res.bodyAsText()
            Log.d("AuthRepository", "Login respuesta: HTTP ${res.status.value} - $responseBody")
            if (res.status.isSuccess()) {
                val tokenRes = Json { ignoreUnknownKeys = true }.decodeFromString<LoginResponse>(responseBody)
                TokenManager.token = tokenRes.token
                Result.success(true)
            } else {
                Result.failure(Exception("HTTP ${res.status.value}: $responseBody"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Excepcion en login", e)
            Result.failure(e)
        }
    }

    suspend fun register(req: RegisterRequest): Result<Boolean> {
        return try {
            Log.d("AuthRepository", "Intentando registro en $baseUrl/register con name=${req.name}, email=${req.email}, rut=${req.rut}")
            val res = client.post("$baseUrl/register") {
                contentType(ContentType.Application.Json)
                setBody(req)
            }
            val responseBody = res.bodyAsText()
            Log.d("AuthRepository", "Registro respuesta: HTTP ${res.status.value} - $responseBody")
            if (res.status.isSuccess()) {
                val tokenRes = Json { ignoreUnknownKeys = true }.decodeFromString<LoginResponse>(responseBody)
                TokenManager.token = tokenRes.token
                Result.success(true)
            } else {
                Result.failure(Exception("HTTP ${res.status.value}: $responseBody"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Excepcion en registro", e)
            Result.failure(e)
        }
    }
}
