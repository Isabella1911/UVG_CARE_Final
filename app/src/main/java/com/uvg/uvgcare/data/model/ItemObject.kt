import kotlinx.serialization.Serializable

@Serializable
data class ItemObject(
    val id: Int,
    val autor: String = "",
    val categoria: String = "",
    val contacto: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val imagen: String = "",
    val autorId: String = "",
    val timestamp: Long = System.currentTimeMillis() // Milisegundos desde el epoch
)
