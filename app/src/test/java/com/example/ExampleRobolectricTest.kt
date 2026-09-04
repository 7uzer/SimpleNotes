package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.Note
import com.example.data.NoteDao
import com.example.data.NoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var repository: NoteRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        noteDao = db.noteDao()
        repository = NoteRepository(noteDao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun read_string_from_context() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Notes", appName)
    }

    @Test
    fun insert_and_read_notes() = runBlocking {
        val note1 = Note(
            title = "Meeting Notes",
            content = "Discuss Q4 objectives and UI redesign.",
            isPinned = false,
            colorIndex = 1
        )
        val note2 = Note(
            title = "Groceries",
            content = "Apples, oats, almond milk, coffee.",
            isPinned = true,
            colorIndex = 2
        )

        repository.insertNote(note1)
        repository.insertNote(note2)

        val notes = repository.allNotes.first()
        assertEquals(2, notes.size)

        // Pinned notes must come first
        assertTrue(notes[0].isPinned)
        assertEquals("Groceries", notes[0].title)
        assertEquals("Meeting Notes", notes[1].title)
    }

    @Test
    fun update_note() = runBlocking {
        val noteId = repository.insertNote(
            Note(title = "Idea", content = "Build a simple app")
        )

        val existing = repository.getNoteById(noteId)
        assertNotNull(existing)

        val updated = existing!!.copy(title = "Updated Idea", isPinned = true)
        repository.updateNote(updated)

        val retrieved = repository.getNoteById(noteId)
        assertEquals("Updated Idea", retrieved?.title)
        assertTrue(retrieved?.isPinned == true)
    }

    @Test
    fun delete_note() = runBlocking {
        val noteId = repository.insertNote(
            Note(title = "Temporary", content = "To be deleted")
        )

        val beforeDelete = repository.allNotes.first()
        assertEquals(1, beforeDelete.size)

        repository.deleteNoteById(noteId)

        val afterDelete = repository.allNotes.first()
        assertEquals(0, afterDelete.size)
    }
}
