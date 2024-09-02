package services

import models.Post

object WallService {
    private var posts = emptyArray<Post>()
    private var nextId = 1

    fun add(post: Post): Post {
        val postWithId = post.copy(id = nextId++)
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
        nextId = 1
    }
}