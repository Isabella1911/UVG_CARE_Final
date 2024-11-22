package com.uvg.uvgcare.firebase.Objects

interface BaseItem {
    val id: Any
    val autor: String
    val categoria: String
    val contacto: String
    val descripcion: String
    val imagen: String
    val autorId: String
    val timestamp: Long
}

data class ItemObject(
    override val id: String,
    override val autor: String = "",
    override val categoria: String = "",
    override val contacto: String = "",
    override val descripcion: String = "",
    override val imagen: String = "",
    override val autorId: String = "",
    override val timestamp: Long = System.currentTimeMillis()
) : BaseItem

// Actualizar el Character para que use la misma interfaz
data class Character(
    override val id: Int,
    override val autor: String,
    override val categoria: String,
    override val contacto: String,
    override val descripcion: String = "",
    override val imagen: String = "",
    override val autorId: String = "",
    override val timestamp: Long = System.currentTimeMillis()
) : BaseItem