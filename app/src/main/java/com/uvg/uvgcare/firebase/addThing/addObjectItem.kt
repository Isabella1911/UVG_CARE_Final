package com.uvg.uvgcare.firebase.addThing

data class ItemObject(
    val id: String = "", // Cambiado de Long a String
    val autor: String = "",
    val autorId: String = "",
    val categoria: String = "",
    val contacto: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val imagen: String = "",
    val timestamp: Long = 0
) {
    companion object {
        fun copy(original: ItemObject, newId: String? = null): ItemObject {
            return ItemObject(
                id = newId ?: original.id,
                autor = original.autor,
                autorId = original.autorId,
                categoria = original.categoria,
                contacto = original.contacto,
                nombre = original.nombre,
                descripcion = original.descripcion,
                imagen = original.imagen,
                timestamp = original.timestamp
            )
        }
    }
}