# Testing Strategy & Verification

- JVM Unit Tests: Robolectric tests for NoteDao (using in-memory Room database), NoteRepository, and NotesViewModel.
- Verification Commands:
  - Compile: `compile_applet`
  - Unit tests: `gradle :app:testDebugUnitTest`
