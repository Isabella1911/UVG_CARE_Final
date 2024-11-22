package com.uvg.uvgcare.firebase.home

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import ItemObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetflixStyleScreen(
    viewModel: NetflixStyleViewModel = viewModel(factory = NetflixStyleViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is NetflixStyleUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
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
                            viewModel = viewModel
                        )
                    }
                    item {
                        CategorySection(
                            categoryName = "Libros",
                            itemObjects = items.filter { it.categoria == "Libros" },
                            viewModel = viewModel
                        )
                    }
                    item {
                        CategorySection(
                            categoryName = "Electrónicos",
                            itemObjects = items.filter { it.categoria == "Electrónicos" },
                            viewModel = viewModel
                        )
                    }
                }
            }
            is NetflixStyleUiState.Error -> {
                val errorMessage = (uiState as NetflixStyleUiState.Error).message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    categoryName: String,
    itemObjects: List<ItemObject>,
    viewModel: NetflixStyleViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = categoryName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
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
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(itemObjects) { obj ->
                ListItem(
                    obj = obj,
                    viewModel = viewModel,
                    onItemClick = { /* Navegar a los detalles */ }
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
            .clickable { onItemClick() },
        shape = RoundedCornerShape(8.dp),
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
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = obj.descripcion,
                style = MaterialTheme.typography.bodySmall
            )
            IconButton(
                onClick = { viewModel.toggleFavorite(obj) }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
