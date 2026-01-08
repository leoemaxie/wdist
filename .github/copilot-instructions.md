# WDIST: WhyDidISaveThis - AI Agent Instructions

## Project Overview
**WhyDidISaveThis** is a Kotlin Multiplatform AI-powered application that analyzes screenshots to recover their forgotten purpose. The app uses LLM-based reasoning to classify screenshot content, infer user intent, and suggest actionable next steps (e.g., calendar events, reminders).

**Core principle:** AI is central to this app; it cannot function without LLM reasoning.

## Architecture Essentials

### Build System: Gradle Kotlin DSL with Kotlin Multiplatform
- **Root:** `settings.gradle.kts` and root `build.gradle.kts` define plugins only; actual config is in `composeApp/build.gradle.kts`
- **Targets:** Android, iOS, Desktop (JVM), Web (Wasm/JS)
- **Version Catalog:** `gradle/libs.versions.toml` manages all dependencies
- **Key tool:** Gradle wrapper (`./gradlew`) for consistent builds

### Multiplatform Structure: `composeApp/src/`
- **`commonMain/`** - Shared Kotlin code (100% of business logic resides here):
  - `org/wdist/com/` houses all core logic: ViewModels, API clients, data models, UI components
  - `networking/` - HTTP client via Ktor, `ApiService` handles `/get-response` calls
  - **Never modify platform-specific code without understanding its wrapper (e.g., `ImagePicker.kt` is an `expect` function with Android/iOS/Desktop `actual` implementations)
- **`androidMain/`, `iosMain/`, `jvmMain/`, etc.** - Platform-specific implementations for file I/O, UI adaptation, and native APIs

### UI Layer: Jetpack Compose Multiplatform
- All UI lives in `commonMain/kotlin/App.kt` and `ui/` subdirectories
- **Compose adoption principle:** Single `@Composable` tree serves all platforms; platform-specific adaptation via `expect/actual`
- **State management:** ViewModels hold mutable state (`var state by mutableStateOf(...)`) and expose it to Composables
- **Key dependency:** Coil for image loading across platforms

## Critical Development Workflows

### Running the App
```bash
# Android (debug APK)
./gradlew :composeApp:assembleDebug

# Desktop (JVM)
./gradlew :composeApp:run

# Web (Wasm - faster, modern browsers)
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Web (JS - slower, broader browser support)
./gradlew :composeApp:jsBrowserDevelopmentRun

# iOS (requires macOS + Xcode)
# Open iosApp/ in Xcode or use IDE run config
```

### Backend Server (Python Flask with Gemini)
- Located at `/server/main.py` - uses Google Gemini's vision API for screenshot analysis on port 5001
- **Client-side endpoint:** `http://127.0.0.1:5001/get-response` (configurable via `ApiService`)
- **Expected flow:** POST binary image → Gemini analyzes intent/action → returns JSON `AIResponse` with `{"content_type", "intent", "action", "action_details"}`
- **Setup:** Requires `GEMINI_API_KEY` in `.env` (get from https://aistudio.google.com/app/apikey)
- **Model:** Uses `gemini-2.0-flash` for fast, cost-effective analysis with built-in vision (no separate OCR needed)

## Project-Specific Patterns & Conventions

### Data Flow: UIState → ViewModel → API → Server
1. **User selects image** → `ImagePicker` composable (platform-specific) → `onImageSelected(ByteArray)`
2. **ViewModel state updates** → Compose recomposes automatically
3. **User clicks "Get Response"** → `MainViewModel.getResponse()` launches coroutine
4. **Network call** → `ApiService.getResponse(image)` posts to Flask backend
5. **Response parsed** → `AIResponse` model (enum-based, serializable) updates state
6. **UI renders** → Text fields, buttons reflect AI response

### Key Models
- **`AIResponse`** (`models.kt`): Structured response from backend with:
  - `contentType`: SCREENSHOT, TEXT, OTHER
  - `intent`: REMINDER, CALENDAR_EVENT, NOTE, DELETE, UNKNOWN
  - `action`: ADD_TO_CALENDAR, SET_REMINDER, SAVE_AS_NOTE, DELETE, NONE
  - `actionDetails`: Optional datetime, title, note for calendar/reminder creation
- Use `@Serializable` annotation (kotlinx.serialization) for all network models

### HTTP Client Setup
- **Ktor is used everywhere** (not OkHttp) for true multiplatform support
- Platform-specific HTTP engine injected via source sets:
  - Android: `ktor-client-android`
  - Desktop: `ktor-client-cio`
  - iOS: `ktor-client-darwin`
  - Web: `ktor-client-js`
- All clients share `ContentNegotiation` plugin for JSON serialization
- `ApiService` accepts configurable `baseUrl` (defaults to `http://127.0.0.1:5001`); can be injected for different environments

### State Management
- **No Redux/MVI used** - direct mutable state pattern with ViewModel
- State mutations happen in coroutines to ensure thread safety
- UI observes state changes via `mutableStateOf()` → automatic recomposition

## Common Pitfalls to Avoid

1. **Platform-specific code in `commonMain`** - Use `expect/actual` mechanism. Example: `ImagePicker.kt` is an `expect` function; each platform provides an `actual` implementation in its source folder.
2. **Missing Gemini API key** - Backend requires `GEMINI_API_KEY` in `/server/.env`. Copy `/server/.env.example` and fill in the key from https://aistudio.google.com/app/apikey.
3. **Missing error handling** - Network calls wrap in try/catch; state stores error message for UI to display. Backend has graceful degradation if Gemini fails.
4. **Gradle dependency conflicts** - Always use version catalog (`libs.versions.toml`); never hardcode versions in `build.gradle.kts`.
5. **Assume image format** - Currently accepts any ByteArray; Gemini handles various formats natively.

## File Organization Quick Reference
- **UI Components:** `composeApp/src/commonMain/kotlin/org/wdist/com/ui/components/`
- **App Theme:** `composeApp/src/commonMain/kotlin/org/wdist/com/ui/theme/`
- **Data Models:** `composeApp/src/commonMain/kotlin/org/wdist/com/models.kt`
- **Networking:** `composeApp/src/commonMain/kotlin/org/wdist/com/networking/`
- **Platform Wrappers:** Individual `*Main` source folders (e.g., `androidMain/`)

## Next Steps for Feature Development

When adding new features:
1. Add serializable data models to `models.kt` first
2. Extend `ApiService` with new endpoint methods
3. Update `MainViewModel` state and methods
4. Design Composable UI in `commonMain/ui/`
5. For platform-specific functionality (permissions, file system), use `expect/actual`
6. Test on at least one platform (Desktop is fastest for iteration)
