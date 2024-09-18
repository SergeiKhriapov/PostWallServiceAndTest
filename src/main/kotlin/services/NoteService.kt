package services

import exceptions.note.NoteCommentNotFoundException
import exceptions.note.NoteNotDeletedException
import exceptions.note.NoteNotFoundException
import models.note.Note
import models.note.NoteComment

object NoteService {
    private var notes = mutableListOf<Note>()
    private var nextNoteId = 1
    private var nextNoteCommentId = 1

    fun clear() {
        notes.clear()
        nextNoteId = 1
        nextNoteCommentId = 1
    }

    fun getNotes(): List<Note> = notes

    fun add(note: Note): Note {
        val noteWithId = note.copy(id = nextNoteId++)
        notes += noteWithId
        return noteWithId
    }

    fun createComment(noteId: Int, comment: NoteComment): NoteComment {
        val note = notes.find { it.id == noteId }
            ?: throw NoteNotFoundException("Заметка с id $noteId не найдена")
        val commentWithId = comment.copy(id = nextNoteCommentId++)
        note.comments += commentWithId
        return commentWithId
    }

    fun delete(noteId: Int): String {
        val note = notes.find { it.id == noteId }
            ?: throw NoteNotFoundException("Заметка с id $noteId не найдена")
        if (note.isDeleted) {
            throw NoteNotFoundException("Заметка с id $noteId удалена")
        }
        note.isDeleted = true
        note.comments.forEach { it.isDeleted = true }
        return "Заметка удалена"
    }

    fun deleteComment(noteId: Int, commentId: Int): String {
        val note = notes.find { it.id == noteId }
            ?: throw NoteNotFoundException("Заметка с id $noteId не найдена")
        if (note.isDeleted) {
            throw NoteNotFoundException("Заметка с id $noteId удалена")
        }
        val comment = note.comments.find { it.id == commentId }
            ?: throw NoteCommentNotFoundException("Комментарий с id $commentId не найден")
        if (comment.isDeleted) {
            throw NoteCommentNotFoundException("Комментарий с id $commentId yдален")
        }
        comment.isDeleted = true
        return "Комментарий удален"
    }

    fun edit(noteId: Int, newTitle: String? = null, newText: String? = null): String {
        val note = notes.find { it.id == noteId }
            ?: throw NoteNotFoundException("Заметка с id $noteId не найдена")
        if (note.isDeleted)
            throw NoteNotFoundException("Заметка с id $noteId удалена")

        val changes = mutableListOf<String>()
        if (newText != null) {
            note.text = newText
            changes.add("Текст изменен на $newText")
        }
        if (newTitle != null) {
            note.title = newTitle
            changes.add("Заголовок изменен на $newTitle")
        }
        if (changes.isEmpty()) {
            return "Изменения не были переданы"
        } else {
            return changes.joinToString(separator = "; ")
        }
    }

    fun editComment(noteId: Int, commentId: Int, newText: String? = null): String {
        val note = notes.find { it.id == noteId }
            ?: throw NoteNotFoundException("Заметка с id $noteId не найдена")
        if (note.isDeleted)
            throw NoteNotFoundException("Заметка с id $noteId удалена")

        val comment = note.comments.find { it.id == commentId }
            ?: throw NoteCommentNotFoundException("Комментарий с  id $commentId не найден")
        if (comment.isDeleted)
            throw NoteCommentNotFoundException("Комментарий с id $commentId удален")

        return if (newText != null) {
            comment.text = newText
            "Текст комментария изменен на $newText"
        } else {
            "Изменения не были переданы"
        }
    }

    fun restore(noteId: Int): String {
        val note = notes.find { it.id == noteId }
            ?: throw NoteNotDeletedException("Заметка с id $noteId не существовала")
        if (!note.isDeleted) throw NoteNotFoundException("Заметка не удалена")

        note.isDeleted = false
        note.comments.forEach { if (it.isDeleted) it.isDeleted = false }
        return "Заметка восстановлена"
    }

    fun restoreComment(noteId: Int, commentId: Int): String {
        val note = notes.find { it.id == noteId }
            ?: throw NoteNotFoundException("Заметка с id $noteId не найдена")
        if (note.isDeleted) {
            throw NoteNotFoundException("Заметка с id $noteId удалена")
        }

        val comment = note.comments.find { it.id == commentId }
            ?: throw NoteCommentNotFoundException("Комментарий с id $commentId не найден")
        if (!comment.isDeleted) {
            throw NoteNotDeletedException("Комментарий с id $commentId не был удален")
        }

        comment.isDeleted = false
        return "Комментарий восстановлен"
    }
}