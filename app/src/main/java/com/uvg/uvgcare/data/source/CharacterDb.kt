package com.uvg.uvgcare.data.source

import com.uvg.uvgcare.data.model.Character
import kotlinx.serialization.Serializable

@Serializable
class CharacterDb {

    private val characters: List<Character> = listOf(
        Character(1, "Nicole", "Laboratorio", "1983 1083", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
        Character(2, "Sam", "Libros", "1308 3763", "https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
        Character(3, "Simon", "Electronicos", "9847 8273", "https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
        Character(4, "Bethany", "Electronicos", "0283 4786", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
        Character(5, "Jaden", "Laboratorio", "0948 8373", "https://rickandmortyapi.com/api/character/avatar/5.jpeg"),
        Character(6, "Lucia", "Electronicos", "8726 4234", "https://rickandmortyapi.com/api/character/avatar/6.jpeg"),
        Character(7, "Abrahm", "Libros", "9374 6238", "https://rickandmortyapi.com/api/character/avatar/7.jpeg"),
        Character(8, "Mia", "Electronicos", "9173 8736", "https://rickandmortyapi.com/api/character/avatar/8.jpeg"),
        Character(9, "Juan", "Laboratorio", "9736 5246", "https://rickandmortyapi.com/api/character/avatar/9.jpeg"),
        Character(10, "Alan", "Electronicos", "7589 7927", "https://rickandmortyapi.com/api/character/avatar/10.jpeg"),
        Character(11, "Albert", "Libros", "8736 4482", "https://rickandmortyapi.com/api/character/avatar/11.jpeg"),
        Character(12, "Alexander", "Laboratorio", "8973 3469", "https://rickandmortyapi.com/api/character/avatar/12.jpeg"),
        Character(13, "Roberto", "Electronicos", "9743 5236", "https://rickandmortyapi.com/api/character/avatar/13.jpeg"),
        Character(14, "Linda", "Laboratorio", "4972 6486", "https://rickandmortyapi.com/api/character/avatar/14.jpeg"),
        Character(15, "Lola", "Electronicos", "1089 3234", "https://rickandmortyapi.com/api/character/avatar/15.jpeg"),
        Character(16, "Amy", "Electronicos", "1974 1398", "https://rickandmortyapi.com/api/character/avatar/16.jpeg"),
        Character(17, "Annie", "Libros", "0945 2937", "https://rickandmortyapi.com/api/character/avatar/17.jpeg"),
        Character(18, "Kenny", "Laboratorio", "1254 9873", "https://rickandmortyapi.com/api/character/avatar/18.jpeg"),
        Character(19, "Miguel", "Electronicos", "0986 6788", "https://rickandmortyapi.com/api/character/avatar/19.jpeg"),
        Character(20, "Ana Paula", "Libros", "4332 2345", "https://rickandmortyapi.com/api/character/avatar/20.jpeg")
    )

    fun getAllCharacters(): List<Character> {
        return characters
    }

    fun getCharacterById(id: Int): Character {
        return characters.first { it.id == id }
    }
}
