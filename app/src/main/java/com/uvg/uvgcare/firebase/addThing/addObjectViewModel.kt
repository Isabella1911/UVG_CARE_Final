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
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    var isLoading by mutableStateOf(false) // Estado de carga
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

    fun updateImageUrl(newUrl: String) {
        if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) {
            imageUrl = newUrl
        } else {
            Log.e("AddItemViewModel", "URL inválido: $newUrl")
        }
    }

    fun toggleDropdown() {
        isDropdownExpanded = !isDropdownExpanded
        println("Estado del menú desplegable: $isDropdownExpanded")
    }

    fun updateCategory(newCategory: String) {
        selectedCategory = newCategory
        isDropdownExpanded = false
        println("Categoría seleccionada: $selectedCategory")
    }

    fun saveItem(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
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
                    id = System.currentTimeMillis().toInt().toString(),
                    autor = repository.auth.currentUser?.email ?: "",
                    autorId = repository.auth.currentUser?.uid ?: "",
                    categoria = selectedCategory,
                    contacto = contact,
                    nombre = name,
                    descripcion = description,
                    imagen = imageUrl,
                    timestamp = System.currentTimeMillis()
                )

                Log.d("AddItemViewModel", "Intentando guardar item: $newItem")

                // Guardar el objeto y obtener el ID
                val result = repository.addItem(newItem)
                if (result.isSuccess) {
                    val itemId = result.getOrNull() ?: throw Exception("No se pudo obtener el ID del objeto guardado.")

                    // Actualizar la lista del usuario
                    repository.updateUserAddList(itemId)

                    onSuccess("¡El objeto se guardó correctamente!")
                } else {
                    Log.e("AddItemViewModel", "Error al guardar: ${result.exceptionOrNull()}")
                    onError(result.exceptionOrNull()?.message ?: "Error desconocido al guardar el objeto.")
                }
            } catch (e: Exception) {
                Log.e("AddItemViewModel", "Exception al guardar: ", e)
                onError(e.message ?: "Ocurrió un error inesperado.")
            } finally {
                isLoading = false
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



