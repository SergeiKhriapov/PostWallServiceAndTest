package models.chat

data class Chat(
    val id: Int,
    val userId: Int, // ID собеседника
    val messages: MutableList<Message> = mutableListOf()
) {
    val unreadMessagesCount: Int
        get() = messages.count { !it.isRead }
}
