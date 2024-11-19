package com.uvg.uvgcare.presentation.mainFlow.Things.favlist
/*
@Composable
fun ListItem(obj: ItemObject) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Image(
                painter = painterResource(id = obj.imagen),
                contentDescription = obj.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = obj.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))


                Text(
                    text = "${obj.categoria} • ${obj.contacto}", // Puedes ajustar según la información que desees mostrar
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = obj.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {
    val objectDb = ObjectDb()
    val favoriteObjects = objectDb.getAllObjects()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
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
            items(favoriteObjects) { obj ->
                ListItem(obj)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewFavoritesScreenLight() {
    UVGCareTheme(darkTheme = false) {
        FavoritesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFavoritesScreenDark() {
    UVGCareTheme(darkTheme = true) {
        FavoritesScreen()
    }
}
*/