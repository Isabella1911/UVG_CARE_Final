package com.uvg.uvgcare.firebase.FavoritesList

data class FavoriteItem(
    val id: String,
    val autor: String = "",
    val categoria: String = "",
    val contacto: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val imagen: String = "",
    val autorId: String = "",
    val timestamp: Long = System.currentTimeMillis() // Milisegundos desde el epoch
)