package com.uvg.uvgcare.presentation.mainFlow.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.uvgcare.data.model.ItemObject

import com.uvg.uvgcare.data.source.ObjectDb
import com.uvg.uvgcare.theme.UVGCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetflixStyleScreen() {
    val objectDb = ObjectDb()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorias") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                val laboratorioObjects = objectDb.getObjectsByCategory("Laboratorio")
                CategorySection(categoryName = "Laboratorio", itemObjects = laboratorioObjects)
            }
            item {
                val librosObjects = objectDb.getObjectsByCategory("Libros")
                CategorySection(categoryName = "Libros", itemObjects = librosObjects)
            }
            item {
                val electronicosObjects = objectDb.getObjectsByCategory("Electronicos")
                CategorySection(categoryName = "Electronicos", itemObjects = electronicosObjects)
            }
        }
    }
}


@Composable
fun CategorySection(categoryName: String, itemObjects: List<com.uvg.uvgcare.data.model.ItemObject>) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = categoryName,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        HorizontalDivider(
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
                ListItem(obj)
            }
        }
    }
}

@Composable
fun ListItem(obj: ItemObject) {

    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .padding(end = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = obj.imagen), // Cargar la imagen desde drawables
                contentDescription = obj.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = obj.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = obj.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            IconButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewNetflixStyleScreenLight() {
    UVGCareTheme(darkTheme = false) { // Tema claro
        NetflixStyleScreen()
    }
}

