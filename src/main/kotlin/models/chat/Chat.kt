package models.chat

data class Chat(
    val id: Int,
    val userId: Int,
    val messages: MutableList<Message> = mutableListOf(),
    var isDeleted: Boolean = false
) {
    val unreadMessagesCount: Int
        get() = messages.count { !it.isRead }
}
