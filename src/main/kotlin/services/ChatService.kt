import exceptions.chat.ChatNotFoundException
import exceptions.chat.MessageNotFoundException
import models.chat.Chat
import models.chat.Message

object ChatService {
    private val chats = mutableListOf<Chat>()
    private var nextChatId = 1

    fun clear() {
        chats.clear()
        nextChatId = 1
    }

    fun createChat(userId: Int): Chat {
        val newChat = Chat(id = nextChatId++, userId = userId)
        chats += newChat
        return newChat
    }

    fun deleteChat(chatId: Int) {
        chats.asSequence()
            .find { it.id == chatId }
            ?.apply { isDeleted = true }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")
    }

    fun getChats(): List<Chat> =
        chats.asSequence()
            .filter { !it.isDeleted }
            .toList()

    fun getUnreadChatsCount(): Int =
        chats.asSequence()
            .count { it.unreadMessagesCount > 0 && !it.isDeleted }

    fun getMessagesFromChat(chatId: Int, count: Int): List<Message> {
        val chat = chats.asSequence()
            .find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")

        val messages = chat.messages.asSequence()
            .filter { !it.isDeleted }
            .toList()

        val lastMessages = if (messages.size <= count) {
            messages
        } else {
            messages.drop(messages.size - count)
        }

        lastMessages.forEach { it.isRead = true }
        return lastMessages.ifEmpty { listOf(Message(id = 0, chatId = chatId, text = "Нет сообщений")) }
    }

    fun addMessage(chatId: Int, message: Message): Message {
        val chat = chats.asSequence()
            .find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")
        val messageWithId = message.copy(id = chat.messages.size + 1, chatId = chatId)
        chat.messages += messageWithId
        return messageWithId
    }

    fun deleteMessage(chatId: Int, messageId: Int) {
        val chat = chats.asSequence()
            .find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")

        val message = chat.messages.asSequence()
            .find { it.id == messageId }
            ?: throw MessageNotFoundException("Сообщение с id $messageId не найдено в чате с id $chatId")

        message.isDeleted = true
    }

    fun updateMessage(chatId: Int, messageId: Int, newText: String) {
        val chat = chats.asSequence()
            .find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")

        val message = chat.messages.asSequence()
            .find { it.id == messageId }
            ?: throw MessageNotFoundException("Сообщение с id $messageId не найдено в чате с id $chatId")

        message.text = newText
    }

    fun getLastMessagesFromChats(): List<String> =
        chats.asSequence()
            .map { chat ->
                val lastMessage = chat.messages.asSequence()
                    .filter { !it.isDeleted }
                    .lastOrNull()
                lastMessage?.text ?: "нет сообщений"
            }
            .toList()
}
