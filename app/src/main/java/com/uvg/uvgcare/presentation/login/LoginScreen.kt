package com.uvg.uvgcare.presentation.login

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvg.uvgcare.theme.UVGCareTheme

@Composable
fun LoginPantalla(
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Login(
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onLoginClick = {
            val message = if (email.isEmpty() || password.isEmpty()) {
                "Error"
            } else {
                "Login"
            }
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        },
        modifier = modifier
    )
}
@Composable
fun Login(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPasswordHidden by rememberSaveable {
        mutableStateOf(true)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ){

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "UVG Care", // Your desired text here
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp), // Add space below the text
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge, // Use appropriate text style
            textAlign = androidx.compose.ui.text.style.TextAlign.Center // Center the text
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = {
                Text(text = "Correo")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = {
                Text(text = "Contraseña")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "¿Olvido su contraseña?",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium, // Use appropriate text style
            textAlign = androidx.compose.ui.text.style.TextAlign.Left // Center the text
        )
        Button(
            onClick = onLoginClick,
            modifier = Modifier.width(140.dp) // Set a fixed width for the button
                ) {
                    Text(text = "Iniciar sesión")
                }
            }
    }

@Preview(showBackground = true)
@Composable
private fun PreviewLogin() {
    UVGCareTheme() {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LoginPantalla()
        }
    }
}
