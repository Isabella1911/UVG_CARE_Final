package com.uvg.uvgcare.firebase.Objects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ItemObject

@Composable
fun ObjectProfileRoute(
    itemId: String,
    onNavigateBack: () -> Unit,
    viewModel: ObjectProfileViewModel = viewModel()
) {
    ObjectProfileScreen(
        itemId = itemId,
        onNavigateBack = onNavigateBack,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectProfileScreen(
    itemId: String,
    onNavigateBack: () -> Unit,
    viewModel: ObjectProfileViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var description by remember { mutableStateOf("") }
    val isFavorite by viewModel.isFavorite.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.checkIfFavorite(itemId)
        viewModel.loadItemDetails(itemId)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {
            is ObjectProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            is ObjectProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as ObjectProfileUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is ObjectProfileUiState.Success -> {
                val item = (uiState as ObjectProfileUiState.Success).item
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Top App Bar
                    LargeTopAppBar(
                        title = {
                            Text(
                                text = item.nombre,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Regresar",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.toggleFavorite(item) }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite
                                    else Icons.Outlined.FavoriteBorder,
                                    contentDescription = if (isFavorite) "Remover de favoritos"
                                    else "Agregar a favoritos",
                                    tint = if (isFavorite) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )

                    // Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Imagen
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            if (item.imagen.isNotEmpty()) {
                                AsyncImage(
                                    model = item.imagen,
                                    contentDescription = "Imagen del objeto",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Image,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Información
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ObjectProfilePropItem(
                                    icon = Icons.Outlined.Category,
                                    title = "Categoría",
                                    value = item.categoria
                                )

                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                                ObjectProfilePropItem(
                                    icon = Icons.Outlined.Person,
                                    title = "Autor",
                                    value = item.autor
                                )

                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                                ObjectProfilePropItem(
                                    icon = Icons.Outlined.Phone,
                                    title = "Contacto",
                                    value = item.contacto
                                )

                                if (item.descripcion.isNotEmpty()) {
                                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                                    ObjectProfilePropItem(
                                        icon = Icons.Outlined.Description,
                                        title = "Descripción",
                                        value = item.descripcion
                                    )
                                }
                            }
                        }

                        // Campo de mensaje
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = {
                                Text(
                                    "Mensaje para el propietario",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Botón de enviar mensaje
                        Button(
                            onClick = { /* Implementar lógica de envío */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Enviar mensaje",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        // Mensaje de favorito
                        AnimatedVisibility(
                            visible = isFavorite,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "¡Agregado a favoritos!",
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ObjectProfilePropItem(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



