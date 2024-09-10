package services

import attachments.*
import models.Comments
import models.Post
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDateTime

class WallServiceTest {
    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun addPost() {
        val post = Post(
            id = 0,
            ownerId = 1,
            fromId = 1,
            date = LocalDateTime.now(),
            text = "Тест функции add",
            replyOwnerId = 1,
            replyPostId = 1,
            comments = Comments(
                0,
                true,
                true,
                true,
                true
            )
        )
        assertNotEquals(0, WallService.add(post).id)
    }

    @Test
    fun updateExistingPost() {
        val post1 = WallService.add(
            Post(
                id = 0,
                ownerId = 1,
                fromId = 1,
                date = LocalDateTime.now(),
                text = "Первый тест функции update",
                replyOwnerId = 1,
                replyPostId = 1,
                comments = Comments(
                    0,
                    true,
                    true,
                    true,
                    true
                )
            )
        )
        val updatePost = post1.copy(text = "Updated text test one")

        val result = WallService.update(updatePost)
        assertTrue(result)
    }

    @Test
    fun updateNonExistingPost() {
        val post1 = WallService.add(
            Post(
                id = 0,
                ownerId = 1,
                fromId = 1,
                date = LocalDateTime.now(),
                text = "Второй тест функции update",
                replyOwnerId = 1,
                replyPostId = 1,
                comments = Comments(0, true, true, true, true)
            )
        )
        val nonExistentPost = post1.copy(id = 999, text = "Updated text test two")

        val result = WallService.update(nonExistentPost)
        assertFalse(result)
    }

    @Test
    fun addPostWithAttachment() {
        val audioAttachmentOne = AttachmentAudio(
            Audio(
                id = 1,
                title = "Аудио",
                url = "http",
                date = LocalDateTime.now()
            )
        )
        val audioAttachmentTwo = AttachmentAudio(
            Audio(
                id = 2,
                title = "Аудио2",
                url = "http",
                date = LocalDateTime.now()
            )
        )

        val post = WallService.add(
            Post(
                id = 15,
                ownerId = 1,
                fromId = 1,
                date = LocalDateTime.now(),
                text = "Тестирование вложений",
                replyOwnerId = 1,
                replyPostId = 1,
                comments = Comments(
                    0,
                    true,
                    true,
                    true,
                    true
                ),
                attachment = listOf(audioAttachmentOne, audioAttachmentTwo)
            )
        )
        assertNotEquals(0, post.id)
        val attachmentOne = post.attachment[0]
        assertTrue(attachmentOne is AttachmentAudio)
        assertEquals("Audio", getTypeAttachment(attachmentOne))

        val attachmentTwo = post.attachment[1]
        assertTrue(attachmentTwo is AttachmentAudio)
        assertEquals("Audio", getTypeAttachment(attachmentTwo))
    }
}