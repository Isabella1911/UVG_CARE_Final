package com.uvg.uvgcare.firebase.FavoritesList

data class FavoriteItem(
    val id: String = "", // ID único del objeto
    val nombre: String = "",
    val categoria: String = "",
    val contacto: String = "",
    val descripcion: String = "",
    val imagenUrl: String = "", // Cambiaremos a URL para imágenes en Firebase Storage
    val timestamp: Long = System.currentTimeMillis() // Para ordenar por fecha
)