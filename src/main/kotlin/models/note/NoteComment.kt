package models.note

data class NoteComment(
    val id: Int,
    val noteId: Int,
    var text: String,
    var isDeleted: Boolean = false
)