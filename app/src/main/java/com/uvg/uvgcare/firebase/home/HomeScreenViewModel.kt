package com.uvg.uvgcare.firebase.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.uvgcare.data.model.ItemObject
import com.uvg.uvgcare.firebase.FavoritesList.FavoriteItem
import com.uvg.uvgcare.firebase.Repository.FirestoreFavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// NetflixStyleUiState.kt
sealed class NetflixStyleUiState {
    data object Loading : NetflixStyleUiState()
    data class Success(val items: List<ItemObject>) : NetflixStyleUiState()
    data class Error(val message: String) : NetflixStyleUiState()
}

class NetflixStyleViewModel(
    private val repository: FirestoreFavoritesRepository = FirestoreFavoritesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<NetflixStyleUiState>(NetflixStyleUiState.Loading)
    val uiState: StateFlow<NetflixStyleUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            try {
                // Replace this with your actual data loading logic
                _uiState.value = NetflixStyleUiState.Success(emptyList())
            } catch (e: Exception) {
                _uiState.value = NetflixStyleUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun isItemFavorite(itemId: String): Flow<Boolean> {
        return repository.isFavorite(itemId)
    }

    fun toggleFavorite(item: ItemObject) {
        viewModelScope.launch {
            try {
                val favoriteItem = FavoriteItem(
                    id = item.id.toString(),
                    nombre = item.nombre,
                    categoria = item.categoria,
                    contacto = item.contacto,
                    descripcion = item.descripcion,
                    imagenUrl = item.imagen.toString()
                )
                val isFavorited = repository.isFavorite(item.id.toString()).first()
                if (isFavorited) {
                    repository.removeFavorite(item.id.toString())
                } else {
                    repository.addFavorite(favoriteItem)
                }
            } catch (e: Exception) {
                // Consider updating UI state to show error
                _uiState.value = NetflixStyleUiState.Error(e.message ?: "Error toggling favorite")
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NetflixStyleViewModel(
                    repository = FirestoreFavoritesRepository())
            }
        }
    }
}