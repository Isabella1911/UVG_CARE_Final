package com.uvg.uvgcare.firebase.addThing

import ItemObject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.uvgcare.firebase.Repository.FirestoreItemRepository
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val repository: FirestoreItemRepository = FirestoreItemRepository()
) : ViewModel() {

    // Campos observables para la pantalla
    var name by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var contact by mutableStateOf("")
        private set
    var selectedCategory by mutableStateOf("")
        private set
    var imageUrl by mutableStateOf("") // URL de la imagen
        private set
    var isDropdownExpanded by mutableStateOf(false)
        private set


    // Categorías disponibles
    val categories = listOf("Laboratorio", "Libros", "Electrónicos")

    // Métodos para actualizar los campos
    fun updateName(newName: String) {
        name = newName
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updateContact(newContact: String) {
        contact = newContact
    }

    fun updateCategory(newCategory: String) {
        selectedCategory = newCategory
        isDropdownExpanded = false // Cerrar el menú después de seleccionar
    }

    fun updateImageUrl(newUrl: String) {
        imageUrl = newUrl
    }

    fun toggleDropdown() {
        isDropdownExpanded = !isDropdownExpanded
        println("Estado del menú desplegable: $isDropdownExpanded")
    }


    // Método para guardar un nuevo objeto en Firestore
    fun saveItem(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Validar campos obligatorios
                if (name.isBlank() || description.isBlank() || contact.isBlank() || selectedCategory.isBlank()) {
                    onError("Todos los campos son obligatorios.")
                    return@launch
                }

                if (imageUrl.isBlank()) {
                    onError("El campo de URL de la imagen no puede estar vacío.")
                    return@launch
                }

                // Crear un nuevo objeto con los datos ingresados
                val newItem = ItemObject(
                    id = 0,
                    autor = repository.auth.currentUser?.email ?: "",
                    autorId = repository.auth.currentUser?.uid ?: "",
                    categoria = selectedCategory,
                    contacto = contact,
                    nombre = name,
                    descripcion = description,
                    imagen = imageUrl, // Guardar el URL ingresado
                    timestamp = System.currentTimeMillis()
                )

                // Intentar guardar el objeto usando el repositorio
                val result = repository.addItem(newItem)
                if (result.isSuccess) {
                    onSuccess()
                } else {
                    onError(result.exceptionOrNull()?.message ?: "Error desconocido al guardar el objeto.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Ocurrió un error inesperado.")
            }
        }
    }

    // Factory para instanciar el ViewModel
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AddItemViewModel(
                    repository = FirestoreItemRepository()
                )
            }
        }
    }
}

