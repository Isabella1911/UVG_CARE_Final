package com.uvg.uvgcare.firebase.addThing

import FirestoreItemRepository
import ItemObject
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val repository: FirestoreItemRepository = FirestoreItemRepository()
) : ViewModel() {

    var name by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var contact by mutableStateOf("")
        private set
    var selectedCategory by mutableStateOf("")
        private set
    var imageUrl by mutableStateOf("")
        private set
    var isDropdownExpanded by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set

    val categories = listOf("Laboratorio", "Libros", "Electrónicos")

    fun updateName(newName: String) {
        name = newName
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updateContact(newContact: String) {
        contact = newContact
    }

    fun updateImageUrl(newUrl: String) {
        if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) {
            imageUrl = newUrl
        } else {
            Log.e("AddItemViewModel", "URL inválido: $newUrl")
        }
    }

    fun toggleDropdown() {
        isDropdownExpanded = !isDropdownExpanded
    }

    fun updateCategory(newCategory: String) {
        selectedCategory = newCategory
        isDropdownExpanded = false
    }

    fun saveItem(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val currentUser = repository.auth.currentUser
                if (currentUser == null) {
                    onError("Usuario no autenticado")
                    return@launch
                }

                if (name.isBlank() || description.isBlank() || contact.isBlank() || selectedCategory.isBlank() || imageUrl.isBlank()) {
                    onError("Todos los campos son obligatorios.")
                    return@launch
                }

                val newItem = ItemObject(
                    id = "",  // El ID será asignado por Firestore
                    autor = currentUser.email ?: "",
                    autorId = currentUser.uid,
                    categoria = selectedCategory,
                    contacto = contact,
                    nombre = name,
                    descripcion = description,
                    imagen = imageUrl,
                    timestamp = System.currentTimeMillis()
                )

                val result = repository.addItem(newItem)
                if (result.isSuccess) {
                    val itemId = result.getOrNull() ?: throw Exception("Error al obtener ID del objeto")
                    repository.updateUserAddList(itemId)
                    onSuccess("¡Objeto guardado exitosamente!")
                } else {
                    throw result.exceptionOrNull() ?: Exception("Error desconocido")
                }
            } catch (e: Exception) {
                Log.e("AddItemViewModel", "Error al guardar", e)
                onError(e.message ?: "Error inesperado al guardar")
            } finally {
                isLoading = false
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AddItemViewModel(FirestoreItemRepository())
            }
        }
    }
}
