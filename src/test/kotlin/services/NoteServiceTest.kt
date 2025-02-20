package services

import exceptions.note.NoteCommentNotFoundException
import exceptions.note.NoteNotDeletedException
import exceptions.note.NoteNotFoundException
import junit.framework.TestCase.assertFalse
import models.note.Note
import models.note.NoteComment
import org.junit.Test

import org.junit.Before
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class NoteServiceTest {
    @Before
    fun clearBeforeTest() {
        NoteService.clear()
    }

    @Test
    fun addedTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        assertNotEquals(0, addedNote.id)
        assertEquals(1, addedNote.id)
        assertEquals("Текст заметки", addedNote.text)
    }

    @Test
    fun createCommentTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        val comment = NoteComment(id = 0, noteId = 1, text = "Тестовый комментарий", isDeleted = false)
        val addedComment = NoteService.createComment(noteId = 1, comment)
        assertNotEquals(0, addedComment.id)
        assertEquals(1, addedComment.id)
        assertEquals("Тестовый комментарий", addedComment.text)
    }

    @Test(expected = NoteNotFoundException::class)
    fun createCommentNoteNotFoundExceptionTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        val comment = NoteComment(
            id = 0,
            noteId = 10,
            text = "Тестовый комментарий"
        )
        NoteService.createComment(10, comment)
    }

    @Test
    fun deleteTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        assertEquals(1, addedNote.id)
        NoteService.delete(1)
        assertEquals(true, addedNote.isDeleted)
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteNoteNotFoundExceptionTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        NoteService.delete(100)
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteNoteNotFoundExceptionTestTwo() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        NoteService.delete(1)
        NoteService.delete(1)
    }

    @Test
    fun deleteCommentTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        val comment = NoteComment(id = 0, noteId = 1, text = "Тестовый комментарий", isDeleted = false)
        val addedComment = NoteService.createComment(noteId = 1, comment)
        assertEquals(1, addedComment.id)
        NoteService.deleteComment(1, 1)
        assertEquals(true, addedComment.isDeleted)
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteCommentNoteNotFoundExceptionTestOne() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        val comment = NoteComment(id = 0, noteId = 1, text = "Тестовый комментарий", isDeleted = false)
        val addedComment = NoteService.createComment(noteId = 1, comment)
        NoteService.deleteComment(10, 1)
    }

    @Test(expected = NoteCommentNotFoundException::class)
    fun deleteCommentNoteCommentNotFoundExceptionTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        val comment = NoteComment(
            id = 0,
            noteId = 0,
            text = "Тестовый комментарий",
            isDeleted = false
        )
        val addedComment = NoteService.createComment(noteId = addedNote.id, comment)
        NoteService.deleteComment(1, 1)
        NoteService.deleteComment(1, 1)
    }

    @Test
    fun editNoteTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        val edited = NoteService.edit(addedNote.id, "Измененная заметка", "Измененный текст")
        assertEquals("Измененная заметка", addedNote.title)
        assertEquals("Измененный текст", addedNote.text)
    }

    @Test
    fun editCommentTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        val comment = NoteComment(id = 0, noteId = 1, text = "Тестовый комментарий")
        val addedComment = NoteService.createComment(noteId = 1, comment)

        val edited = NoteService.editComment(1, 1, "Измененный комментарий")
        assertEquals("Измененный комментарий", addedComment.text)
    }

    @Test
    fun restoreNoteTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки",
                isDeleted = true
            )
        )
        val restored = NoteService.restore(addedNote.id)
        assertFalse(addedNote.isDeleted)
    }

    @Test
    fun restoreCommentTest() {
        val addedNote = NoteService.add(
            Note(
                id = 0,
                title = "Заметка",
                text = "Текст заметки"
            )
        )
        val comment = NoteComment(id = 0, noteId = 1, text = "Тестовый комментарий", isDeleted = true)
        val addedComment = NoteService.createComment(noteId = 1, comment)

        val restored = NoteService.restoreComment(1, 1)
        assertFalse(addedComment.isDeleted)
    }
}