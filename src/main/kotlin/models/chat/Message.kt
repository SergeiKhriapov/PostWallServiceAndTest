package models.chat

data class Message(
    val id: Int,
    val chatId: Int,
    var text: String,
    var isRead: Boolean = false
)
