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

    // Estado para la lista de favoritos
    private val _favorites = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favorites: StateFlow<List<FavoriteItem>> = _favorites.asStateFlow()

    // Estado UI
    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                _uiState.value = FavoritesUiState.Loading
                repository.getFavorites().collect { favoritesList ->
                    _favorites.value = favoritesList
                    _uiState.value = FavoritesUiState.Success(favoritesList)
                }
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun addFavorite(item: FavoriteItem) {
        viewModelScope.launch {
            try {
                repository.addFavorite(item)
                // No necesitamos actualizar _favorites aquí porque el Flow en getFavorites()
                // automáticamente notificará los cambios
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error("Error al añadir favorito: ${e.message}")
            }
        }
    }

    fun removeFavorite(itemId: String) {
        viewModelScope.launch {
            try {
                repository.removeFavorite(itemId)
                // No necesitamos actualizar _favorites aquí porque el Flow en getFavorites()
                // automáticamente notificará los cambios
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error("Error al eliminar favorito: ${e.message}")
            }
        }
    }

    // Método para refrescar la lista de favoritos manualmente si es necesario
    fun refreshFavorites() {
        loadFavorites()
    }

    // Método para limpiar los errores si es necesario
    fun clearError() {
        if (uiState.value is FavoritesUiState.Error) {
            _uiState.value = FavoritesUiState.Success(_favorites.value)
        }
    }
}