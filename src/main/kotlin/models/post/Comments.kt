package models.post

data class Comments(
    val id: Int,
    val ownerID: Int,
    val text: String,
    var reports: MutableList<CommentReport> = mutableListOf()
)
