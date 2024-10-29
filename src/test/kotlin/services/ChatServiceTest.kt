package services

import exceptions.chat.ChatNotFoundException
import exceptions.chat.MessageNotFoundException
import models.chat.Message
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

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
        assertEquals(0, ChatService.getUnreadChatsCount())

        ChatService.addMessage(chat1.id, Message(id = 0, chatId = chat1.id, text = "Hello!"))
        assertEquals(1, ChatService.getUnreadChatsCount())
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
    fun deleteMessageChatNotFoundExceptionTest() {
        val message = Message(id = 0, chatId = 0, text = "Hello!")
        val chat = ChatService.createChat(userId = 1)
        ChatService.addMessage(chat.id, message)

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
        val chat = ChatService.createChat(userId = 1)
        val originalMessage = Message(id = 0, chatId = chat.id, text = "Hello!")
        val addedMessage = ChatService.addMessage(chat.id, originalMessage)

        val newText = "Updated Message"
        ChatService.updateMessage(chat.id, addedMessage.id, newText)

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

    @Test
    fun getLastMessagesFromChatsTest() {
        val chat1 = ChatService.createChat(userId = 1)
        ChatService.addMessage(chat1.id, Message(id = 1, chatId = chat1.id, text = "Привет!", isDeleted = false))
        ChatService.addMessage(chat1.id, Message(id = 2, chatId = chat1.id, text = "Как дела?", isDeleted = false))
        ChatService.addMessage(
            chat1.id,
            Message(id = 3, chatId = chat1.id, text = "Последнее сообщение", isDeleted = true)
        )

        val chat2 = ChatService.createChat(userId = 2)
        ChatService.addMessage(
            chat2.id,
            Message(id = 4, chatId = chat2.id, text = "Удаленное сообщение", isDeleted = true)
        )

        val chat3 = ChatService.createChat(userId = 3)

        val expected = listOf(
            "Как дела?",            // chat1
            "нет сообщений",        // chat2
            "нет сообщений"         // chat3
        )

        val result = ChatService.getLastMessagesFromChats()
        assertEquals(expected, result)
    }
}
