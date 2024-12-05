package an.imation.myapplication.Chat

data class Message(
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long
)