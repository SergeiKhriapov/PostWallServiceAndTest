package services

import attachments.*
import exceptions.CommentNotFoundException
import exceptions.InvalidReportException
import exceptions.PostNotFoundException
import models.CommentReport
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
            replyPostId = 1
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
                replyPostId = 1
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
                replyPostId = 1
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
                id = 1, title = "Аудио", url = "http", date = LocalDateTime.now()
            )
        )
        val audioAttachmentTwo = AttachmentAudio(
            Audio(
                id = 2, title = "Аудио2", url = "http", date = LocalDateTime.now()
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

    @Test
    fun addCommentPostExists() {
        val post = WallService.add(
            Post(
                id = 11,
                ownerId = 1,
                fromId = 1,
                date = LocalDateTime.now(),
                text = "Sample post text",
                replyOwnerId = 1,
                replyPostId = 1
            )
        )
        val comment = Comments(id = 0, ownerID = 1, text = "Test comment")
        val createdComment = WallService.createComment(1, comment)
        assertNotEquals(0, createdComment.id)
        assertEquals("Test comment", createdComment.text)
    }

    @Test
    fun reportCommentSuccessfully() {
        val post = WallService.add(
            Post(
                id = 1,
                ownerId = 1,
                fromId = 1,
                date = LocalDateTime.now(),
                replyOwnerId = 0,
                replyPostId = 0
            )
        )
        val comment = WallService.createComment(post.id, Comments(id = 0, ownerID = 1, text = "Test comment"))
        val report = CommentReport(
            ownerId = 1,
            commentId = comment.id,
            spam = true
        )

        WallService.reportComment(comment.id, report)

        val updatedPost = WallService.getPostById(post.id)
        assertNotNull(updatedPost)
        val reportedComment = updatedPost!!.comments.find { it.id == comment.id }
        assertNotNull(reportedComment)
        assertTrue(reportedComment!!.reports.any { it.spam })
        assertEquals(1, reportedComment.reports.size)
    }

    @Test(expected = InvalidReportException::class)
    fun throwInvalidReportExceptionIfAllFalseAreFalseOrNull() {
        val post = WallService.add(
            Post(
                id = 1,
                ownerId = 1,
                fromId = 1,
                date = LocalDateTime.now(),
                replyOwnerId = 0,
                replyPostId = 0
            )
        )
        val comment = WallService.createComment(post.id, Comments(id = 0, ownerID = 1, text = "Test comment"))
        val report = CommentReport(
            ownerId = 1,
            commentId = comment.id,
            spam = false,
            violence = false,
            other = null
        )

        WallService.reportComment(comment.id, report)
    }

    @Test(expected = CommentNotFoundException::class)
    fun throwCommentNotFoundExceptionIfCommentDoesNotExist() {
        val report = CommentReport(
            ownerId = 1,
            commentId = 2,
            spam = true
        )

        WallService.reportComment(2, report)
    }
}