# Dev Container Optimizations for KMP in Codespaces

## What's Optimized

### 1. **Memory Allocation (Codespaces-tuned)**
- Gradle JVM: 3GB (balanced for cloud resources)
- Kotlin Daemon: 1.5GB
- No container limits (Codespaces manages this)

### 2. **Build Performance**
- **Gradle daemon enabled**: Keeps build process warm between builds
- **Parallel builds**: Enables parallel compilation of independent modules
- **Configuration cache**: Speeds up subsequent builds by ~50%
- **Build cache**: Reuses outputs from previous builds

### 3. **Targets Enabled**
- ✅ **JVM (Desktop)** - Primary development target, fastest builds
- ✅ **iOS Native** - Kotlin/Native compilation support
- ⚠️ **Android** - Disabled in Codespaces (no Android SDK)

> **Note:** Android builds are not supported in GitHub Codespaces due to SDK limitations. 
> Focus on JVM/Desktop development in the cloud. For Android development, use a local dev container.

### 4. **Python Backend Support**
- Python 3.11 installed via feature
- Auto-installs Flask dependencies on container creation
- VS Code Python extensions included

### 5. **IDE Performance**
- Build folders excluded from search
- Proper Java/Kotlin language server configuration
- Python language server (Pylance) for backend work

## Quick Commands

```bash
# Desktop (JVM) - Fastest for development iteration ⭐ RECOMMENDED
./gradlew :composeApp:run

# Run Python Backend
cd server && python3 main.py

# Build all non-Android targets
./gradlew build

# Clean build (if issues arise)
./gradlew clean build

# Check Gradle configuration
./gradlew --status
```

## Rebuilding the Container

After these changes, rebuild your dev container:
1. Open Command Palette (`Cmd/Ctrl+Shift+P`)
2. Run: **Codespaces: Rebuild Container**

## Android Development

To build Android targets, you'll need a local environment:
```bash
# On your local machine with Android SDK installed
./gradlew :composeApp:assembleDebug
```

Or set up a local dev container with full Android SDK support.
