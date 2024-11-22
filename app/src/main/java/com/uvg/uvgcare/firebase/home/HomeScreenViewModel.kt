package com.uvg.uvgcare.firebase.home

import FirestoreItemRepository
import ItemObject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.uvgcare.firebase.FavoritesList.FavoriteItem
import com.uvg.uvgcare.firebase.Repository.FirestoreFavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Estado para manejar la UI del catálogo principal
sealed class NetflixStyleUiState {
    data object Loading : NetflixStyleUiState()
    data class Success(val items: List<ItemObject>) : NetflixStyleUiState()
    data class Error(val message: String) : NetflixStyleUiState()
}

class NetflixStyleViewModel(
    private val favoritesRepository: FirestoreFavoritesRepository = FirestoreFavoritesRepository(),
    private val itemsRepository: FirestoreItemRepository = FirestoreItemRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<NetflixStyleUiState>(NetflixStyleUiState.Loading)
    val uiState: StateFlow<NetflixStyleUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    // Método para cargar todos los objetos desde Firestore
    private fun loadItems() {
        viewModelScope.launch {
            try {
                _uiState.value = NetflixStyleUiState.Loading
                val result = itemsRepository.getAllItems()
                if (result.isSuccess) {
                    _uiState.value = NetflixStyleUiState.Success(result.getOrThrow())
                } else {
                    _uiState.value = NetflixStyleUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
                }
            } catch (e: Exception) {
                _uiState.value = NetflixStyleUiState.Error(e.message ?: "Error inesperado al cargar los objetos")
            }
        }
    }

    // Método para verificar si un objeto está en favoritos
    fun isItemFavorite(itemId: String): Flow<Boolean> {
        return favoritesRepository.isFavorite(itemId)
    }

    // Método para alternar entre añadir o eliminar un objeto de favoritos
    fun toggleFavorite(item: ItemObject) {
        viewModelScope.launch {
            try {
                // Usamos favoritesRepository que ya está instanciado en el ViewModel
                val userId = favoritesRepository.getCurrentUserId()

                if (userId == null) {
                    _uiState.value = NetflixStyleUiState.Error("No hay usuario autenticado")
                    return@launch
                }

                val favoriteItem = FavoriteItem(
                    id = item.id.toString(),
                    nombre = item.nombre,
                    categoria = item.categoria,
                    contacto = item.contacto,
                    descripcion = item.descripcion,
                    imagen = item.imagen
                )

                // Verificamos si está en favoritos usando el Flow
                val isFavorited = favoritesRepository.isFavorite(item.id.toString()).first()

                if (isFavorited) {
                    // Si ya está en favoritos, lo removemos
                    favoritesRepository.removeFavoriteFromUser(userId, item.id.toString())
                } else {
                    // Si no está en favoritos, lo agregamos
                    favoritesRepository.addFavorite(userId, favoriteItem)
                }
            } catch (e: Exception) {
                _uiState.value = NetflixStyleUiState.Error(e.message ?: "Error al actualizar favoritos")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NetflixStyleViewModel(
                    favoritesRepository = FirestoreFavoritesRepository(),
                    itemsRepository = FirestoreItemRepository()
                )
            }
        }
    }
}
