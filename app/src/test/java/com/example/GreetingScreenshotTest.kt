package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.Note
import com.example.data.NoteRepository
import com.example.ui.NotesScreen
import com.example.ui.NotesViewModel
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    private lateinit var db: AppDatabase
    private lateinit var repository: NoteRepository
    private lateinit var viewModel: NotesViewModel

    @Before
    fun setup() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = NoteRepository(db.noteDao())

        // Add sample notes for the preview test
        repository.insertNote(
            Note(
                title = "Design Principles",
                content = "Emphasize negative space, typography, and soothing pastel cards.",
                isPinned = true,
                colorIndex = 1
            )
        )
        repository.insertNote(
            Note(
                title = "Shopping List",
                content = "1. Sourdough loaf\n2. Oat milk\n3. Olive oil\n4. Dark chocolate",
                isPinned = false,
                colorIndex = 2
            )
        )

        viewModel = NotesViewModel(repository)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun notes_screen_screenshot() {
        composeTestRule.setContent {
            MyApplicationTheme {
                NotesScreen(viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
    }
}
