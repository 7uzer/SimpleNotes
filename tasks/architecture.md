# Technical Architecture

```
com.example
├── data
│   ├── Note.kt (Room Entity)
│   ├── NoteDao.kt (Room DAO)
│   ├── AppDatabase.kt (Room Database)
│   └── NoteRepository.kt (Repository)
├── ui
│   ├── theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── NotesViewModel.kt (StateFlow & business logic)
│   ├── NotesScreen.kt (Single-view UI, Note cards, Search & Filter)
│   └── NoteEditSheet.kt (ModalBottomSheet for creating/editing note)
└── MainActivity.kt
```
- Data Flow:
  - Database (SQLite / Room) -> DAO returns Flow<List<Note>> -> Repository -> NotesViewModel filters by search & color -> UiState -> Compose UI
  - User Action -> ViewModel (suspend) -> Repository -> DAO
