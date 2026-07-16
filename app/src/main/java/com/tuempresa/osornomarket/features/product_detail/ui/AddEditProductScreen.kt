package com.tuempresa.osornomarket.features.product_detail.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuempresa.osornomarket.core.model.Product
import com.tuempresa.osornomarket.features.product_detail.viewmodel.ProductDetailState
import com.tuempresa.osornomarket.features.product_detail.viewmodel.ProductDetailViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Nuevo") }

    // Si la operación fue exitosa, volvemos atrás
    LaunchedEffect(uiState) {
        if (uiState is ProductDetailState.Success) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (productId == null) "Agregar Producto" else "Editar Producto") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Tipo de Producto") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de la Imagen") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = condition,
                onValueChange = { condition = it },
                label = { Text("Condición (Nuevo, Usado...)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val parsedId = productId?.toIntOrNull() ?: 0
                    val product = Product(
                        id = parsedId,
                        name = name,
                        brand = brand,
                        description = description,
                        price = price.toLongOrNull() ?: 0L,
                        type = type,
                        sellerId = "", // Asignado por el backend
                        imageUrl = imageUrl,
                        condition = condition
                    )
                    viewModel.saveProduct(product)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is ProductDetailState.Loading
            ) {
                if (uiState is ProductDetailState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar Producto")
                }
            }

            if (productId != null) {
                TextButton(
                    onClick = { 
                        productId.toIntOrNull()?.let { viewModel.deleteProduct(it) } 
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar Producto")
                }
            }
        }
    }
}
