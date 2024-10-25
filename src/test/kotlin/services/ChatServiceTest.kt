package services

import exceptions.chat.ChatNotFoundException
import exceptions.chat.MessageNotFoundException
import models.chat.Message
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertFailsWith

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun createChatTest() {
        val chat = ChatService.createChat(userId = 1)
        assertNotEquals(0, chat.id)
        assertEquals(1, chat.id)
        assertEquals(1, chat.userId)
    }

    @Test
    fun deleteChatTest() {
        val chat = ChatService.createChat(userId = 1)
        ChatService.deleteChat(chat.id)
        assertFailsWith<ChatNotFoundException> {
            ChatService.deleteChat(chat.id)
        }
    }

    @Test
    fun deleteChatNotFoundExceptionTest() {
        assertFailsWith<ChatNotFoundException> {
            ChatService.deleteChat(999)
        }
    }

    @Test
    fun getChatsTest() {
        ChatService.createChat(userId = 1)
        ChatService.createChat(userId = 2)
        val chats = ChatService.getChats()
        assertEquals(2, chats.size)
    }

    @Test
    fun getUnreadChatsCountTest() {
        val chat1 = ChatService.createChat(userId = 1)
        ChatService.createChat(userId = 2)

        val unreadCount = ChatService.getUnreadChatsCount()
        assertEquals(0, unreadCount)
    }

    @Test
    fun addMessageTest() {
        val chat = ChatService.createChat(userId = 1)
        val message = Message(id = 0, chatId = 0, text = "Hello!")
        val addedMessage = ChatService.addMessage(chat.id, message)

        assertNotEquals(0, addedMessage.id)
        assertEquals(1, addedMessage.id)
        assertEquals("Hello!", addedMessage.text)
    }

    @Test
    fun addMessageChatNotFoundExceptionTest() {
        val message = Message(id = 0, chatId = 0, text = "Hello!")
        assertFailsWith<ChatNotFoundException> {
            ChatService.addMessage(999, message)
        }
    }

    @Test
    fun deleteMessageTest() {
        val chat = ChatService.createChat(userId = 1)
        val message = Message(id = 0, chatId = 0, text = "Hello!")
        val addedMessage = ChatService.addMessage(chat.id, message)

        ChatService.deleteMessage(chat.id, addedMessage.id)
        assertFailsWith<MessageNotFoundException> {
            ChatService.deleteMessage(chat.id, addedMessage.id)
        }
    }

    @Test
    fun deleteMessageChatNotFoundExceptionTest() {
        val message = Message(id = 0, chatId = 0, text = "Hello!")
        ChatService.createChat(userId = 1)
        ChatService.addMessage(1, message)

        assertFailsWith<ChatNotFoundException> {
            ChatService.deleteMessage(999, message.id)
        }
    }

    @Test
    fun deleteMessageNotFoundExceptionTest() {
        val chat = ChatService.createChat(userId = 1)
        val message = Message(id = 0, chatId = 0, text = "Hello!")
        ChatService.addMessage(chat.id, message)

        assertFailsWith<MessageNotFoundException> {
            ChatService.deleteMessage(chat.id, 999)
        }
    }

    @Test
    fun getMessagesFromChatTest() {
        val chat = ChatService.createChat(userId = 1)
        ChatService.addMessage(chat.id, Message(id = 0, chatId = chat.id, text = "Hello!"))
        ChatService.addMessage(chat.id, Message(id = 0, chatId = chat.id, text = "World!"))

        val messages = ChatService.getMessagesFromChat(chat.id, 2)
        assertEquals(2, messages.size)
        assertEquals("Hello!", messages[0].text)
        assertEquals("World!", messages[1].text)
    }

    @Test
    fun getMessagesFromChatChatNotFoundExceptionTest() {
        assertFailsWith<ChatNotFoundException> {
            ChatService.getMessagesFromChat(999, 1)
        }
    }

    @Test
    fun getMessagesFromChatEmptyTest() {
        val chat = ChatService.createChat(userId = 1)
        val messages = ChatService.getMessagesFromChat(chat.id, 1)
        assertEquals(1, messages.size)
        assertEquals("Нет сообщений", messages[0].text)
    }

    @Test
    fun updateMessageSuccessTest() {
        // Создаем чат и добавляем сообщение
        val chat = ChatService.createChat(userId = 1)
        val originalMessage = Message(id = 0, chatId = chat.id, text = "Hello!")
        val addedMessage = ChatService.addMessage(chat.id, originalMessage)

        // Обновляем сообщение
        val newText = "Updated Message"
        ChatService.updateMessage(chat.id, addedMessage.id, newText)

        // Проверяем, что текст сообщения обновился
        val updatedMessage = ChatService.getMessagesFromChat(chat.id, 1).first()
        assertEquals(newText, updatedMessage.text)
    }

    @Test
    fun updateMessageChatNotFoundExceptionTest() {
        assertFailsWith<ChatNotFoundException> {
            ChatService.updateMessage(999, 1, "New Text")
        }
    }

    @Test
    fun updateMessageNotFoundExceptionTest() {
        val chat = ChatService.createChat(userId = 1)
        val originalMessage = Message(id = 0, chatId = chat.id, text = "Hello!")
        ChatService.addMessage(chat.id, originalMessage)
        assertFailsWith<MessageNotFoundException> {
            ChatService.updateMessage(chat.id, 999, "New Text")
        }
    }

    @Test
    fun updateMessageWithEmptyTextTest() {
        val chat = ChatService.createChat(userId = 1)
        val originalMessage = Message(id = 0, chatId = chat.id, text = "Hello!")
        val addedMessage = ChatService.addMessage(chat.id, originalMessage)
        val newText = ""
        ChatService.updateMessage(chat.id, addedMessage.id, newText)
        val updatedMessage = ChatService.getMessagesFromChat(chat.id, 1).first()
        assertEquals(newText, updatedMessage.text)
    }
}
