package com.uvg.uvgcare.data.source

import com.uvg.uvgcare.R
import com.uvg.uvgcare.data.model.ItemObject

class ObjectDb {

    private val itemObjects: List<ItemObject> = listOf  (
        ItemObject(1, "Nicole", "Laboratorio", "1983 1083", "Bata de Laboratorio","Es una bata", R.drawable.bata1),
        ItemObject(2, "Antonio", "Laboratorio", "9381 4273", "Bata de otro laboratorio","Es una bata (de otro lugar)", R.drawable.bata2),
        ItemObject(3, "Isabel", "Laboratorio", "3213 1083", "Bata de especial","Es una bata (pero especial)", R.drawable.bata3),
        ItemObject(4, "Manolo", "Libros", "1983 5543", "Libro Fisica 1","Eli es un buen profe", R.drawable.librofisica1),
        ItemObject(5, "Marcos", "Libros", "3456 1083", "Libro Fisica 2","Eli es un buen profe pt. 2", R.drawable.librofisica2),
        ItemObject(6, "Carlos", "Libros", "1983 2344", "Libro Fisica 3","Eli no da fisica 3", R.drawable.librofisica2),
        ItemObject(7, "Mejilla", "Electronicos", "1234 1083", "Super Calculadora Pasco","Odio a los electronicos pasco", R.drawable.calcpasco1),
        ItemObject(8, "Frida", "Electronicos", "5422 1083", "Voltimetro","Mide voltaje y corriente", R.drawable.voltimetro2),
        ItemObject(9, "Fuentes", "Electronicos", "1983 5678", "Agujero Negro","Casualmente de la masa de la tierra", R.drawable.agujero3),
    )

    fun getAllObjects(): List<ItemObject> {
            return itemObjects
    }

    fun getObjectById(id: Int): ItemObject {
            return itemObjects.first { it.id == id }
    }

    fun getObjectsByCategory(category: String): List<ItemObject> {
        return itemObjects.filter { it.categoria == category }
    }

}