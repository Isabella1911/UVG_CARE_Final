package com.uvg.uvgcare.firebase.Objects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.uvgcare.firebase.FavoritesList.FavoriteItem
import com.uvg.uvgcare.firebase.Repository.FirestoreFavoritesRepository
import com.uvg.uvgcare.firebase.utils.AuthUtils
import ItemObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore

sealed class ObjectProfileUiState {
    object Loading : ObjectProfileUiState()
    data class Success(val item: ItemObject) : ObjectProfileUiState()
    data class Error(val message: String) : ObjectProfileUiState()
}

class ObjectProfileViewModel : ViewModel() {
    private val repository = FirestoreFavoritesRepository()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow<ObjectProfileUiState>(ObjectProfileUiState.Loading)
    val uiState: StateFlow<ObjectProfileUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadItemDetails(itemId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = ObjectProfileUiState.Loading

                firestore.collection("items")
                    .document(itemId)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val item = document.toObject(ItemObject::class.java)
                            if (item != null) {
                                _uiState.value = ObjectProfileUiState.Success(item)
                            } else {
                                _uiState.value = ObjectProfileUiState.Error("No se pudo cargar el objeto")
                            }
                        } else {
                            _uiState.value = ObjectProfileUiState.Error("Objeto no encontrado")
                        }
                    }
                    .addOnFailureListener { e ->
                        _uiState.value = ObjectProfileUiState.Error(e.message ?: "Error desconocido")
                    }
            } catch (e: Exception) {
                _uiState.value = ObjectProfileUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun checkIfFavorite(itemId: String) {
        viewModelScope.launch {
            try {
                repository.isFavorite(itemId).collect { isFav ->
                    _isFavorite.value = isFav
                }
            } catch (e: Exception) {
                println("Error checking favorite status: ${e.message}")
            }
        }
    }

    fun toggleFavorite(item: ItemObject) {
        viewModelScope.launch {
            try {
                val userId = AuthUtils.requireCurrentUserId()

                val favoriteItem = FavoriteItem(
                    id = item.id.toString(),
                    nombre = item.nombre,
                    categoria = item.categoria,
                    contacto = item.contacto,
                    descripcion = item.descripcion,
                    imagen = item.imagen,
                    autorId = item.autorId,
                    timestamp = item.timestamp
                )

                if (_isFavorite.value) {
                    repository.removeFavoriteFromUser(userId, item.id.toString())
                } else {
                    repository.addFavorite(userId, favoriteItem)
                }

                _isFavorite.value = !_isFavorite.value
            } catch (e: Exception) {
                println("Error toggling favorite: ${e.message}")
            }
        }
    }
}