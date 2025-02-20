package models.post

data class CommentReport(
    val ownerId: Int,
    val commentId: Int,
    val spam: Boolean = false,
    val violence: Boolean = false,
    val other: String? = null
)