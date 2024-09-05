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
    fun addPostWithPhotoAndVideoAttachments() {
        val photoAttachment = AttachmentPhoto(
            id = 1,
            title = "Фото для поста",
            photoData = Photo(
                id = 11,
                title = "Море",
                URL = "https://ya.ru/images/search?from=tabbar&img_url=https%3A%2F%2Fget.pxhere.com%2Fphoto%2Fsea-water-ocean-wave-underwater-biology-lagoon-coral-reef-reef-snorkeling-florida-keys-shoal-wind-wave-marine-biology-looe-key-1390347.jpg&lr=109044&pos=2&rpt=simage&text=%D0%9C%D0%BE%D1%80%D0%B5"
            ),
            date = LocalDateTime.now()
        )
        val videoAttachment = AttachmentVideo(
            id = 2,
            title = "Видео для поста",
            videoData = Video(
                id = 33,
                title = "Горы",
                URL = "https://ya.ru/images/search?from=tabbar&img_url=https%3A%2F%2Fget.pxhere.com%2Fphoto%2Fsea-water-ocean-wave-underwater-biology-lagoon-coral-reef-reef-snorkeling-florida-keys-shoal-wind-wave-marine-biology-looe-key-1390347.jpg&lr=109044&pos=2&rpt=simage&text=%D0%9C%D0%BE%D1%80%D0%B5"
            ),
            date = LocalDateTime.now()
        )
        val post = Post(
            id = 0,
            ownerId = 1,
            fromId = 1,
            date = LocalDateTime.now(),
            text = "Post with photo and video attachments",
            replyOwnerId = 1,
            replyPostId = 1,
            comments = Comments(
                count = 0,
                canPost = true,
                groupsCanPost = true,
                canClose = true,
                canOpen = true
            ),
            attachment = listOf(photoAttachment, videoAttachment)
        )
        val addedPost = WallService.add(post)
        assertNotEquals(0, addedPost.id)
        assertEquals(2, addedPost.attachment.size)

    }
}