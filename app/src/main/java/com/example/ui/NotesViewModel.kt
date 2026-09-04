package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.Note
import com.example.data.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val totalCount: Int = 0,
    val searchQuery: String = "",
    val selectedColorFilter: Int? = null,
    val showPinnedOnly: Boolean = false,
    val editingNote: Note? = null,
    val isCreatingNew: Boolean = false
)

private data class FilterCriteria(
    val query: String,
    val colorFilter: Int?,
    val pinnedOnly: Boolean
)

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedColorFilter = MutableStateFlow<Int?>(null)
    val selectedColorFilter: StateFlow<Int?> = _selectedColorFilter.asStateFlow()

    private val _showPinnedOnly = MutableStateFlow(false)
    val showPinnedOnly: StateFlow<Boolean> = _showPinnedOnly.asStateFlow()

    private val _editingNote = MutableStateFlow<Note?>(null)
    val editingNote: StateFlow<Note?> = _editingNote.asStateFlow()

    private val _isCreatingNew = MutableStateFlow(false)
    val isCreatingNew: StateFlow<Boolean> = _isCreatingNew.asStateFlow()

    private val _recentlyDeletedNote = MutableStateFlow<Note?>(null)
    val recentlyDeletedNote: StateFlow<Note?> = _recentlyDeletedNote.asStateFlow()

    private val filterCriteria: Flow<FilterCriteria> = combine(
        _searchQuery,
        _selectedColorFilter,
        _showPinnedOnly
    ) { query, colorFilter, pinnedOnly ->
        FilterCriteria(query, colorFilter, pinnedOnly)
    }

    private val filteredNotes: Flow<List<Note>> = combine(
        repository.allNotes,
        filterCriteria
    ) { allNotes, filter ->
        val trimmedQuery = filter.query.trim()
        allNotes.filter { note ->
            val matchesQuery = if (trimmedQuery.isEmpty()) {
                true
            } else {
                note.title.contains(trimmedQuery, ignoreCase = true) ||
                        note.content.contains(trimmedQuery, ignoreCase = true)
            }
            val matchesColor = filter.colorFilter == null || note.colorIndex == filter.colorFilter
            val matchesPinned = !filter.pinnedOnly || note.isPinned
            matchesQuery && matchesColor && matchesPinned
        }
    }

    val uiState: StateFlow<NotesUiState> = combine(
        repository.allNotes,
        filteredNotes,
        filterCriteria,
        _editingNote,
        _isCreatingNew
    ) { allNotes, filtered, filter, editing, isNew ->
        NotesUiState(
            notes = filtered,
            totalCount = allNotes.size,
            searchQuery = filter.query,
            selectedColorFilter = filter.colorFilter,
            showPinnedOnly = filter.pinnedOnly,
            editingNote = editing,
            isCreatingNew = isNew
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState()
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onClearSearch() {
        _searchQuery.value = ""
    }

    fun onColorFilterSelected(colorIndex: Int?) {
        _selectedColorFilter.value = if (_selectedColorFilter.value == colorIndex) null else colorIndex
    }

    fun onTogglePinnedFilter() {
        _showPinnedOnly.value = !_showPinnedOnly.value
    }

    fun startCreateNote(initialColorIndex: Int = 0) {
        _editingNote.value = Note(
            id = 0,
            title = "",
            content = "",
            isPinned = false,
            colorIndex = initialColorIndex
        )
        _isCreatingNew.value = true
    }

    fun startEditNote(note: Note) {
        _editingNote.value = note
        _isCreatingNew.value = false
    }

    fun dismissEditor() {
        _editingNote.value = null
        _isCreatingNew.value = false
    }

    fun saveNote(
        title: String,
        content: String,
        isPinned: Boolean,
        colorIndex: Int
    ) {
        val trimmedTitle = title.trim()
        val trimmedContent = content.trim()

        // Don't save empty notes
        if (trimmedTitle.isEmpty() && trimmedContent.isEmpty()) {
            dismissEditor()
            return
        }

        val current = _editingNote.value
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (_isCreatingNew.value || current == null || current.id == 0L) {
                val newNote = Note(
                    title = trimmedTitle,
                    content = trimmedContent,
                    isPinned = isPinned,
                    colorIndex = colorIndex,
                    createdAt = now,
                    updatedAt = now
                )
                repository.insertNote(newNote)
            } else {
                val updatedNote = current.copy(
                    title = trimmedTitle,
                    content = trimmedContent,
                    isPinned = isPinned,
                    colorIndex = colorIndex,
                    updatedAt = now
                )
                repository.updateNote(updatedNote)
            }
            dismissEditor()
        }
    }

    fun togglePin(note: Note) {
        viewModelScope.launch {
            repository.updateNote(
                note.copy(
                    isPinned = !note.isPinned,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            _recentlyDeletedNote.value = note
            if (_editingNote.value?.id == note.id) {
                dismissEditor()
            }
        }
    }

    fun restoreDeletedNote() {
        val noteToRestore = _recentlyDeletedNote.value ?: return
        viewModelScope.launch {
            repository.insertNote(noteToRestore)
            _recentlyDeletedNote.value = null
        }
    }

    fun clearRecentlyDeleted() {
        _recentlyDeletedNote.value = null
    }

    companion object {
        fun provideFactory(repository: NoteRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(repository) as T
                }
            }
    }
}
