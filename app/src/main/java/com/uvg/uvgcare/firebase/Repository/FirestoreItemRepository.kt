package com.uvg.uvgcare.firebase.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.uvg.uvgcare.data.model.ItemObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FirestoreItemRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private fun getUserItemsRef() = db.collection("items")

    suspend fun addItem(item: ItemObject): Result<String> = withContext(Dispatchers.IO) {
        try {
            val itemData = hashMapOf<String, Any>(  // Especificamos los tipos explícitamente
                "id" to item.id,
                "nombre" to item.nombre,
                "categoria" to item.categoria,
                "descripcion" to item.descripcion,
                "contacto" to item.contacto,
                "autor" to (auth.currentUser?.email ?: ""),
                "autorId" to (auth.currentUser?.uid ?: ""),
                "imagen" to item.imagen,
                "timestamp" to FieldValue.serverTimestamp()
            )

            val docRef = getUserItemsRef().add(itemData).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getItems(): Flow<List<ItemObject>> = callbackFlow {
        val subscription = getUserItemsRef()
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val items = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(ItemObject::class.java)?.copy(
                            id = doc.get("id") as? Int ?: 0
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(items)
            }

        awaitClose { subscription.remove() }
    }
}

