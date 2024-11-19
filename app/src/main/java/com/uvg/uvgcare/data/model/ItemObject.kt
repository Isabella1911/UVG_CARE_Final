package com.uvg.uvgcare.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemObject(
    val id: Int,
    val autor: String = "",
    val categoria: String = "",
    val contacto: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val imagen: Int = 0
)
