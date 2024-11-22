import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@Serializable
@IgnoreExtraProperties
data class ItemObject(
    val id: String = "", // Firebase requiere valores predeterminados
    val autor: String = "",
    val categoria: String = "",
    val contacto: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val imagen: String = "",
    val autorId: String = "",
    val timestamp: Long = 0L // Valor predeterminado para compatibilidad
)
