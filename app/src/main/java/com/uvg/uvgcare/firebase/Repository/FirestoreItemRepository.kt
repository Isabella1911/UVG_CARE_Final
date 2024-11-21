import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreItemRepository {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val itemsCollection = firestore.collection("items")

    suspend fun addItem(item: ItemObject): Result<String> {
        return try {
            // Verificar si el usuario está autenticado
            val currentUser = auth.currentUser
            if (currentUser == null) {
                return Result.failure(Exception("Usuario no autenticado"))
            }

            // Crear un nuevo documento con ID automático
            val newDoc = itemsCollection.document()

            // Crear el objeto con el ID del documento
            val itemWithId = com.uvg.uvgcare.firebase.addThing.ItemObject(
                id = newDoc.id,
                autor = currentUser.email ?: "",
                autorId = currentUser.uid,
                categoria = item.categoria,
                contacto = item.contacto,
                nombre = item.nombre,
                descripcion = item.descripcion,
                imagen = item.imagen,
                timestamp = System.currentTimeMillis()
            )

            // Guardar el documento
            newDoc.set(itemWithId).await()

            Result.success(newDoc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItemsByUser(userId: String): Result<List<ItemObject>> {
        return try {
            val snapshot = itemsCollection
                .whereEqualTo("autorId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val items = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ItemObject::class.java)
            }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllItems(): Result<List<ItemObject>> {
        return try {
            val snapshot = itemsCollection
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val items = snapshot.documents.mapNotNull { doc ->
                doc.toObject(ItemObject::class.java)
            }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}