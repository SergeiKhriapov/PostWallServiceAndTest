package models.note

data class Note(
    val id: Int,
    var title: String,
    var text: String,
    var comments: MutableList<NoteComment> = mutableListOf(),
    var isDeleted: Boolean = false
)