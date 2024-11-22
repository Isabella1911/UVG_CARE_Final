package com.uvg.uvgcare.firebase.FavoritesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.uvgcare.firebase.Repository.FirestoreFavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado UI para manejar diferentes estados de la pantalla
sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    data class Success(val favorites: List<FavoriteItem>) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}

class FavoritesViewModel : ViewModel() {
    private val repository = FirestoreFavoritesRepository()

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                _uiState.value = FavoritesUiState.Loading
                val userId = repository.getCurrentUserId()
                if (userId == null) {
                    _uiState.value = FavoritesUiState.Error("Usuario no autenticado")
                    return@launch
                }

                repository.getUserFavorites(userId).collect { favoritesList ->
                    _uiState.value = FavoritesUiState.Success(favoritesList)
                }
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun removeFavorite(itemId: String) {
        viewModelScope.launch {
            try {
                val userId = repository.getCurrentUserId() ?: return@launch
                repository.removeFavoriteFromUser(userId, itemId)
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error("Error al eliminar favorito: ${e.message}")
            }
        }
    }
}
