package com.uvg.uvgcare.presentation.mainFlow.addThing

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
import androidx.compose.material.icons.filled.Image
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
    Box(
        modifier = Modifier.fillMaxSize() // Box to take up full screen
    ) {
        Column(
            modifier = modifier
                .padding(bottom = 80.dp) // Add padding to avoid overlapping with bottom buttons
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
                        text = "Crear Objeto",
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
                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    placeholder = { Text("Descripción del objeto") },
                    modifier = Modifier.fillMaxWidth()  // Standard full-width text box
                )
                Spacer(modifier = Modifier.height(16.dp))
                CharacterProfilePropItem(
                    title = "Autor:",
                    value = character.autor,
                    modifier = Modifier.fillMaxWidth()
                )
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

                // Box for the image icon above the button
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)  // Adjust the size of the icon box
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))  // Optional background for visual effect
                            .align(Alignment.Center) // Aligns the icon box to center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Image, // Changed icon to image icon
                            contentDescription = "Image Icon",
                            modifier = Modifier
                                .fillMaxSize()  // Make icon fill the box
                                .padding(16.dp),  // Padding around the icon
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Centered button below the icon
                Button(
                    onClick = { /* Handle button click */ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Agregar imágenes")
                }
            }
        }

        // Bottom buttons placed in a row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)  // Padding for buttons
                .align(Alignment.BottomCenter),  // Aligns row to the bottom of the screen
            horizontalArrangement = Arrangement.SpaceBetween  // Space between buttons
        ) {
            Button(
                onClick = { /* Handle left button click */ },
                modifier = Modifier.weight(1f)  // Equal width buttons
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(16.dp))  // Space between buttons
            Button(
                onClick = { /* Handle right button click */ },
                modifier = Modifier.weight(1f)  // Equal width buttons
            ) {
                Text("Guardar")
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
