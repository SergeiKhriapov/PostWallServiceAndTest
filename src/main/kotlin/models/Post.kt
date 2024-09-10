package models

import attachments.AttachmentAudio
import java.time.LocalDateTime

data class Post(
    val id: Int,
    val ownerId: Int,
    val fromId: Int,
    val createdBy: Int? = null,
    val date: LocalDateTime,
    var text: String? = null,
    val replyOwnerId: Int,
    val replyPostId: Int,
    var friendsOnly: Boolean = false,
    var comments: Comments,
    var likes: Likes = Likes(),
    var isPinned: Boolean = false,
    var markedAsAds: Boolean = false,
    var isFavorite: Boolean = false,
    var attachment: List<AttachmentAudio> = emptyList() // вариант с хэшкодом
)
