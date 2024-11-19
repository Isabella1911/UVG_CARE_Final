package com.uvg.uvgcare.firebase.Repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uvg.uvgcare.firebase.FavoritesList.FavoriteItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreFavoritesRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun getUserFavoritesRef() = db.collection("users")
        .document(auth.currentUser?.uid ?: "")
        .collection("favorites")

    fun isFavorite(itemId: String): Flow<Boolean> = callbackFlow {
        val subscription = getUserFavoritesRef()
            .document(itemId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.exists() ?: false)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun addFavorite(item: FavoriteItem) {
        try {
            getUserFavoritesRef().document(item.id).set(item)
        } catch (e: Exception) {
            Log.e("Firestore", "Error adding favorite", e)
            throw e
        }
    }
    fun getFavorites(): Flow<List<FavoriteItem>> = callbackFlow {
        val subscription = getUserFavoritesRef()
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val favorites = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FavoriteItem::class.java)
                } ?: emptyList()

                trySend(favorites)
            }
        awaitClose { subscription.remove() }
    }
    suspend fun removeFavorite(itemId: String) {
        try {
            getUserFavoritesRef().document(itemId).delete()
        } catch (e: Exception) {
            Log.e("Firestore", "Error removing favorite", e)
            throw e
        }
    }
}