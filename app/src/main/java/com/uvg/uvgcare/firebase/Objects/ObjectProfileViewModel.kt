package com.uvg.uvgcare.firebase.Objects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.uvgcare.data.model.Character
import com.uvg.uvgcare.firebase.FavoritesList.FavoriteItem
import com.uvg.uvgcare.firebase.Repository.FirestoreFavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterProfileViewModel : ViewModel() {
    private val repository = FirestoreFavoritesRepository()
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun checkIfFavorite(itemId: String) {
        viewModelScope.launch {
            try {
                repository.isFavorite(itemId).collect { isFav ->
                    _isFavorite.value = isFav
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            try {
                // Obtener el ID del usuario actual
                val userId = repository.getCurrentUserId()

                // Verificar si tenemos un usuario válido
                if (userId == null) {
                    println("Error: No hay usuario autenticado")
                    return@launch
                }

                if (_isFavorite.value) {
                    // Si ya es favorito, lo removemos
                    repository.removeFavorite(userId, character.id.toString())
                } else {
                    // Si no es favorito, lo agregamos
                    val favoriteItem = FavoriteItem(
                        id = character.id.toString(),
                        nombre = character.autor,
                        categoria = character.categoria,
                        contacto = character.contacto,
                        descripcion = "", // Campo vacío por defecto
                        imagen = character.imagen
                    )
                    repository.addFavorite(userId, favoriteItem)
                }
                // Actualizamos el estado local
                _isFavorite.value = !_isFavorite.value
            } catch (e: Exception) {
                // Manejar el error
                println("Error toggling favorite: ${e.message}")
            }
        }
    }
}