package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.AppDatabase
import com.example.data.NoteRepository
import com.example.ui.NotesScreen
import com.example.ui.NotesViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val notesViewModel: NotesViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = NoteRepository(database.noteDao())
        NotesViewModel.provideFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NotesScreen(viewModel = notesViewModel)
            }
        }
    }
}
