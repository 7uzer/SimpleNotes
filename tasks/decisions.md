# Architectural & Technical Decisions

- **Single Screen Architecture**: Per single-view constraint for simple tools, use a single screen with responsive search, filter chips, and a ModalBottomSheet note editor instead of multi-screen navigation stack.
- **Room Database with Flow**: Reactive data persistence returning `Flow<List<Note>>` directly from DAO to Repository and ViewModel `stateIn`.
- **Predefined Soft Palette**: 6 modern muted pastel accent colors (Neutral, Sage, Lavender, Amber, Sky, Rose) for visual distinction of notes without harsh saturated colors.
- **Pinning & Sorting**: Pinned notes automatically appear on top; within each section, sorted by updated timestamp descending.
