package com.uvg.uvgcare.data.model
import kotlinx.serialization.Serializable
@Serializable
class Character(
    val id: Int,
    val autor: String,
    val categoria: String,
    val contacto: String,
    val imagen: String
)
