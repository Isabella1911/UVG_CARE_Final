package com.uvg.uvgcare.firebase.addThing

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
    var description by mutableStateOf("")
        private set

    var contact by mutableStateOf("")
        private set

    var isDropdownExpanded by mutableStateOf(false)
        private set

    val categories = listOf("Laboratorio", "Libros", "Electronicos")

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updateContact(newContact: String) {
        contact = newContact
    }

    fun toggleDropdown() {
        isDropdownExpanded = !isDropdownExpanded
    }

    fun selectImage() {
        // Implementar selección de imagen
    }

    fun saveItem() {
        viewModelScope.launch {
            // Implementar guardado en Firestore
        }
    }

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