package com.tuempresa.osornomarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tuempresa.osornomarket.features.auth.ui.LoginScreen
import com.tuempresa.osornomarket.features.auth.ui.RegisterScreen
import com.tuempresa.osornomarket.features.home.ui.HomeScreen
import com.tuempresa.osornomarket.features.product_detail.ui.AddEditProductScreen
import com.tuempresa.osornomarket.ui.theme.OsornoMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OsornoMarketTheme {
                // Navegación ultra simple con auth:
                var currentScreen by remember { mutableStateOf("login") }

                when (currentScreen) {
                    "login" -> {
                        LoginScreen(
                            onNavigateToRegister = { currentScreen = "register" },
                            onLoginSuccess = { currentScreen = "home" }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            onNavigateToLogin = { currentScreen = "login" },
                            onRegisterSuccess = { currentScreen = "home" }
                        )
                    }
                    "home" -> {
                        HomeScreen(
                            onNavigateToAddProduct = { currentScreen = "add_product" }
                        )
                    }
                    "add_product" -> {
                        AddEditProductScreen(
                            onNavigateBack = { currentScreen = "home" }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OsornoMarketTheme {
        Greeting("Android")
    }
}