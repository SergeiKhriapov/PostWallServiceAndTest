package services

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
        val chat = chats.find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")
        chats.remove(chat)
    }

    fun getChats(): List<Chat> = chats

    fun getUnreadChatsCount(): Int = chats.count { it.unreadMessagesCount > 0 }

    fun getMessagesFromChat(chatId: Int, count: Int): List<Message> {
        val chat = chats.find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")
        // Помечаем все сообщения как прочитанные
        chat.messages.forEach { it.isRead = true }
        return chat.messages.takeLast(count)
            .ifEmpty { listOf(Message(id = 0, chatId = chatId, text = "Нет сообщений")) }
    }

    fun addMessage(chatId: Int, message: Message): Message {
        val chat = chats.find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")
        val messageWithId = message.copy(id = chat.messages.size + 1, chatId = chatId)
        chat.messages += messageWithId
        return messageWithId
    }

    fun deleteMessage(chatId: Int, messageId: Int) {
        val chat = chats.find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")

        val message = chat.messages.find { it.id == messageId }
            ?: throw MessageNotFoundException("Сообщение с id $messageId не найдено в чате с id $chatId")
        chat.messages.remove(message)
    }
    fun updateMessage(chatId: Int, messageId: Int, newText: String) {
        val chat = chats.find { it.id == chatId }
            ?: throw ChatNotFoundException("Чат с id $chatId не найден")

        val message = chat.messages.find { it.id == messageId }
            ?: throw MessageNotFoundException("Сообщение с id $messageId не найдено в чате с id $chatId")
        message.text = newText
    }
}
