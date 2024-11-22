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

    //fun toggleFavorite(character: Character) {
    //    viewModelScope.launch {
    //        try {
    //            if (_isFavorite.value) {
    //                repository.removeFavorite(character.id.toString())
    //            } else {
    //                val favoriteItem = FavoriteItem(
    //                    id = character.id.toString(),
    //                    nombre = character.autor,
    //                    categoria = character.categoria,
    //                    contacto = character.contacto,
    //                    descripcion = "",
    //                    imagen = character.imagen
    //                )
    //                repository.addFavorite(favoriteItem)
    //            }
    //            _isFavorite.value = !_isFavorite.value
    //        } catch (e: Exception) {
    //            // Handle error
    //        }
    //    }
    //
}


