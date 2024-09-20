package com.uvg.uvgcare.presentation.Things.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import com.uvg.uvgcare.data.model.Character
import com.uvg.uvgcare.data.source.CharacterDb
import com.uvg.uvgcare.theme.UVGCareTheme

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
private fun CharacterProfileScreen(
    character: Character,
    onNavigateBack: () -> Unit,
    text: String, // Text field value passed as parameter
    onTextChange: (String) -> Unit, // Text change handler passed as parameter
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Full-width navigation space with text aligned left
        Box(
            modifier = Modifier
                .fillMaxWidth()  // Full width
                .height(56.dp)   // Standard app bar height
                .background(MaterialTheme.colorScheme.primary)  // Background color
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))  // Small space between icon and text
                Text(
                    text = "Nombre del Objeto",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
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

            // Wider box for character icon
            Box(
                modifier = Modifier
                    .width(300.dp)   // Adjust width here
                    .height(200.dp)  // Adjust height here
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

            // Regular text box (no internal state management)
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("Descripción del objeto") },
                modifier = Modifier.fillMaxWidth()  // Standard full-width text box
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Centered button
            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Solicitar")
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
    var text = ""  // External text state management
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
                onTextChange = { text = it }, // Handle text change
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

