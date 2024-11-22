package com.uvg.uvgcare.firebase.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.uvg.uvgcare.firebase.FavoritesList.FavoriteItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class FirestoreFavoritesRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    suspend fun addFavorite(userId: String, item: FavoriteItem) {
        try {
            // Crear una subcolección "favorites" para el usuario
            val userFavoritesRef = usersCollection
                .document(userId)
                .collection("favorites")
                .document(item.id)

            // Convertir el FavoriteItem a un Map
            val favoriteData = mapOf(
                "id" to item.id,
                "nombre" to item.nombre,
                "categoria" to item.categoria,
                "contacto" to item.contacto,
                "descripcion" to item.descripcion,
                "imagen" to item.imagen,
                "autorId" to item.autorId,
                "timestamp" to item.timestamp
            )

            userFavoritesRef.set(favoriteData).await()
        } catch (e: Exception) {
            throw Exception("Error al agregar favorito: ${e.message}")
        }
    }

    // Actualizar el método de verificación
    fun isFavorite(itemId: String): Flow<Boolean> {
        val userId = getCurrentUserId() ?: return flowOf(false)
        return callbackFlow {
            val registration = usersCollection
                .document(userId)
                .collection("favorites")
                .document(itemId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(false)
                        return@addSnapshotListener
                    }
                    trySend(snapshot?.exists() == true)
                }
            awaitClose { registration.remove() }
        }
    }

    // Actualizar el método de eliminación
    suspend fun removeFavoriteFromUser(userId: String, itemId: String) {
        try {
            usersCollection
                .document(userId)
                .collection("favorites")
                .document(itemId)
                .delete()
                .await()
        } catch (e: Exception) {
            throw Exception("Error al eliminar el favorito: ${e.message}")
        }
    }

    // Actualizar el método para obtener favoritos
    fun getUserFavorites(userId: String): Flow<List<FavoriteItem>> = callbackFlow {
        val listener = usersCollection
            .document(userId)
            .collection("favorites")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val favorites = snapshot?.documents?.map { doc ->
                    FavoriteItem(
                        id = doc.id,
                        nombre = doc.getString("nombre") ?: "",
                        categoria = doc.getString("categoria") ?: "",
                        contacto = doc.getString("contacto") ?: "",
                        descripcion = doc.getString("descripcion") ?: "",
                        imagen = doc.getString("imagen") ?: "",
                        autorId = doc.getString("autorId") ?: "",
                        timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                    )
                } ?: emptyList()

                trySend(favorites)
            }

        awaitClose { listener.remove() }
    }
}

