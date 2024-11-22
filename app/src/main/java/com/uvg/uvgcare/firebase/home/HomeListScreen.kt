package com.uvg.uvgcare.firebase.home

import ItemObject
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetflixStyleScreen(
    viewModel: NetflixStyleViewModel = viewModel(factory = NetflixStyleViewModel.Factory),
    onItemClick: (String) -> Unit = {}  // Nuevo parámetro para la navegación
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Categorías",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (uiState) {
                is NetflixStyleUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                is NetflixStyleUiState.Success -> {
                    val items = (uiState as NetflixStyleUiState.Success).items

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        item {
                            CategorySection(
                                categoryName = "Laboratorio",
                                itemObjects = items.filter { it.categoria == "Laboratorio" },
                                viewModel = viewModel,
                                onItemClick = onItemClick  // Pasar el callback
                            )
                        }
                        item {
                            CategorySection(
                                categoryName = "Libros",
                                itemObjects = items.filter { it.categoria == "Libros" },
                                viewModel = viewModel,
                                onItemClick = onItemClick  // Pasar el callback
                            )
                        }
                        item {
                            CategorySection(
                                categoryName = "Electrónicos",
                                itemObjects = items.filter { it.categoria == "Electrónicos" },
                                viewModel = viewModel,
                                onItemClick = onItemClick  // Pasar el callback
                            )
                        }
                    }
                }
                is NetflixStyleUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (uiState as NetflixStyleUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    categoryName: String,
    itemObjects: List<ItemObject>,
    viewModel: NetflixStyleViewModel,
    onItemClick: (String) -> Unit  // Nuevo parámetro
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = categoryName,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(itemObjects) { obj ->
                ListItem(
                    obj = obj,
                    viewModel = viewModel,
                    onItemClick = { onItemClick(obj.id.toString()) }  // Pasar el ID del objeto
                )
            }
        }
    }
}

@Composable
fun ListItem(
    obj: ItemObject,
    viewModel: NetflixStyleViewModel,
    onItemClick: () -> Unit
) {
    val isFavorite by viewModel.isItemFavorite(obj.id.toString()).collectAsState(initial = false)

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .padding(end = 16.dp)
            .clickable(onClick = onItemClick),  // Usar el callback
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(data = obj.imagen),
                contentDescription = obj.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = obj.nombre,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1
            )
            Text(
                text = obj.descripcion,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                ),
                maxLines = 2
            )
            IconButton(
                onClick = { viewModel.toggleFavorite(obj) }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


