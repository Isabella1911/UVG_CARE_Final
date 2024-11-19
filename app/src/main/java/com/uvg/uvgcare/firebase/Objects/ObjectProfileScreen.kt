package com.uvg.uvgcare.firebase.Objects

//import androidx.compose.material.icons.outlined.Image
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.uvgcare.data.model.Character
import com.uvg.uvgcare.data.source.CharacterDb
import com.uvg.uvgcare.theme.UVGCareTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState

@Composable
fun CharacterProfileRoute(
    id: Int,
    onNavigateBack: () -> Unit
) {
    val characterDb = CharacterDb()
    val character = characterDb.getCharacterById(id)
    CharacterProfileScreen(
        character = character,
        onNavigateBack = onNavigateBack,
        text = "",
        onTextChange = { /* Handle text change */ },
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterProfileScreen(
    character: Character,
    onNavigateBack: () -> Unit,
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterProfileViewModel = viewModel<CharacterProfileViewModel>()
) {
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(character.id) {
        viewModel.checkIfFavorite(character.id.toString())
    }

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Nombre del Objeto",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                // Botón de favorito
                IconButton(
                    onClick = { viewModel.toggleFavorite(character) }
                ) {
                    Icon(
                        imageVector = if (isFavorite)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite)
                            "Remover de favoritos"
                        else
                            "Agregar a favoritos",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Person",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = character.autor,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            CharacterProfilePropItem(
                title = "Categoría:",
                value = character.categoria,
                modifier = Modifier.fillMaxWidth()
            )

            CharacterProfilePropItem(
                title = "Contacto:",
                value = character.contacto,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("Descripción del objeto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Solicitar")
            }

            // Mostrar un mensaje cuando se agrega/remueve de favoritos
            AnimatedVisibility(
                visible = isFavorite,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = "¡Agregado a favoritos!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}


@Composable
private fun CharacterProfilePropItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Text(text = value)
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCharacterProfileScreen() {
    var text = ""
    UVGCareTheme {
        Surface {
            CharacterProfileScreen(
                character = Character(
                    id = 2565,
                    autor = "Nicole",
                    categoria = "Laboratorio",
                    contacto = "2442 5642",
                    imagen = "",
                ),
                onNavigateBack = { },
                text = text,
                onTextChange = { text = it },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

