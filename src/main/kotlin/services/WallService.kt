package services

import exceptions.CommentNotFoundException
import exceptions.InvalidReportException
import exceptions.PostNotFoundException
import models.CommentReport
import models.Comments
import models.Post

object WallService {
    private var posts = emptyArray<Post>()

    //    private var comments = emptyArray<Comments>()
    private var nextIdPost = 1
    private var nextIdComment = 1
    private var nextReportId = 1

    fun add(post: Post): Post {
        val postWithId = post.copy(id = nextIdPost++)
        posts += postWithId
        return postWithId
    }

    fun update(post: Post): Boolean {
        for ((index, existingPost) in posts.withIndex()) {
            if (existingPost.id == post.id) {
                posts[index] = post.copy(
                    ownerId = existingPost.ownerId,
                    fromId = existingPost.fromId,
                    createdBy = existingPost.createdBy,
                    date = existingPost.date
                )
                return true
            }
        }
        return false
    }

    fun clear() {
        posts = emptyArray()
//        comments = emptyArray<Comments>()
        nextIdPost = 1
        nextIdComment = 1
        nextReportId = 1
    }


    fun createComment(postId: Int, comment: Comments): Comments {
        val postIndex = posts.indexOfFirst { it.id == postId }
        if (postIndex == -1) {
            throw PostNotFoundException("Пост с ID $postId не найден")
        }
        val post = posts[postIndex]
        val commentWithId = comment.copy(id = nextIdComment++)
        val updatedPost = post.copy(comments = post.comments + commentWithId)
        posts[postIndex] = updatedPost
        return commentWithId
    }


    fun reportComment(commentId: Int, report: CommentReport) {
        if (report.spam == false && report.violence == false && report.other == null) {
            throw InvalidReportException("Хотя бы одно из полей должно быть указано: spam, violence, other")
        }
        for (post in posts) {
            val comment = post.comments.find { it.id == commentId }
            if (comment != null) {
                comment.reports.add(report.copy(commentId = comment.id, ownerId = post.ownerId))
                return
            }
        }
        throw CommentNotFoundException("Комментарий с ID $commentId не найден")
    }

    fun getPostById(id: Int): Post? {
        return posts.find { it.id == id }
    }
}