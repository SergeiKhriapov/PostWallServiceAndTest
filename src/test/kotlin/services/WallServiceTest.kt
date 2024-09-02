package services

import models.Comments
import models.Post
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

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
            date = 12345,
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
                date = 12345,
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
                date = 12345,
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
}