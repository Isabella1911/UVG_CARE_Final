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
    private val db = FirebaseFirestore.getInstance()

    // Obtener el ID del usuario actual
    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    // Agregar un ítem a la lista de favoritos del usuario
    suspend fun addFavorite(userId: String, item: FavoriteItem) {
        try {
            val userDoc = usersCollection.document(userId)
            userDoc.update("favorites", FieldValue.arrayUnion(item))
                .await()
        } catch (e: Exception) {
            throw Exception("Error al agregar favorito: ${e.message}")
        }
    }

    // Remover un ítem de la lista de favoritos del usuario
    suspend fun removeFavorite(userId: String, itemId: String) {
        try {
            val userDoc = usersCollection.document(userId)
            userDoc.update("favorites", FieldValue.arrayRemove(itemId))
                .await()
        } catch (e: Exception) {
            throw Exception("Error al eliminar favorito: ${e.message}")
        }
    }

    // Verificar si un ítem está en la lista de favoritos
    fun isFavorite(itemId: String): Flow<Boolean> {
        val userId = getCurrentUserId() ?: return flowOf(false)
        return callbackFlow {
            val userDoc = usersCollection.document(userId)
            val registration = userDoc.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(false)
                    return@addSnapshotListener
                }
                val favorites = snapshot?.get("favorites") as? List<String> ?: emptyList()
                trySend(favorites.contains(itemId))
            }
            awaitClose { registration.remove() }
        }
    }

    // Obtener la lista de favoritos del usuario
    fun getUserFavorites(userId: String): Flow<List<FavoriteItem>> = callbackFlow {
        val favoritesRef = db.collection("users").document(userId).collection("favorites")

        val listener = favoritesRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            val favorites = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(FavoriteItem::class.java)
            } ?: emptyList()

            trySend(favorites).isSuccess
        }

        awaitClose { listener.remove() }
    }

    suspend fun removeFavoriteFromUser(userId: String, itemId: String) {
        try {
            val userDoc = usersCollection.document(userId)
            userDoc.update("favorites", FieldValue.arrayRemove(itemId))
                .await()
        } catch (e: Exception) {
            throw Exception("Error al eliminar el favorito: ${e.message}")
        }
    }



}

