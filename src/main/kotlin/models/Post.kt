package models

data class Post(
    val id: Int,                // id поста
    val ownerId: Int,           // id владельца стены
    val fromId: Int,            // id автора поста
    val createdBy: Int? = null, // Идентификатор администратора, который опубликовал запись (если применимо)
    val date: Int,
    val text: String? = null,
    val replyOwnerId: Int,
    val replyPostId: Int,
    val friendsOnly: Boolean = false,
    val comments: Comments,
    val likes: Likes = Likes(),
    val isPinned: Boolean = false,
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false
)
