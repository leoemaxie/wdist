# WhyDidISaveThis (WDIST)
*Recover the intent behind your screenshots.*

---

## 📝 Concept

**WhyDidISaveThis** is a cross-platform AI assistant that helps users recover the **forgotten purpose behind their screenshots** and take meaningful next actions.  

People take screenshots to remember important information, track ideas, or save fleeting moments. Yet most screenshots are **never revisited**, and their original intent is quickly forgotten. This app bridges that gap using AI to analyze, explain, and suggest actionable next steps.

---

## 🎯 Problem Statement

Modern users often struggle with screenshots because:

- Screenshots are **quick to capture but hard to retrieve**.  
- The brain “outsources memory” when taking screenshots, weakening recall.  
- Mobile galleries are **poorly organized**, mixing screenshots with photos.  
- Users lose track of what information needs attention.

The result: **digital clutter and missed actions**.

---

## 💡 Solution

**WhyDidISaveThis** uses AI to:

1. **Understand the Screenshot:**  
   - OCR extracts text.  
   - Classifies content: receipt, message, booking, error, social media, or reminder-like content.

2. **Recover Likely Intent:**  
   - Explains why the screenshot was taken in a natural, human-friendly way.  
   - Example:  
     > Screenshot: “Delivery confirmed — Jan 18”  
     > AI: “You likely saved this to track a package.”

3. **Offer a Single Action (Optional Calendar Integration):**  
   - Add event to calendar (if date detected)  
   - Set a reminder  
   - Save as a note  
   - Delete if no longer needed  

> Every action is optional; no automation occurs without user confirmation.

---

## 🖥️ Platforms

Built with **Kotlin Multiplatform**:

- **Shared (common code):**
  - Screenshot analysis & OCR
  - AI intent inference
  - Action decision logic
  - Screenshot history data model

- **Platform-specific:**
  - **Android:** File access, notifications, calendar integration  
  - **Desktop/Web:** Screenshot upload, preview, optional `.ics` calendar export  

---

## 🤖 AI Component

- **OCR:** Extracts text from images  
- **LLM-based reasoning:** Classifies screenshot type and infers intent  
- **Action agent:** Suggests **one clear next step**, optionally adding events to calendar  

> AI is **central** — the app cannot function without it.

---

## 🎬 Demo Flow

1. User drags or uploads a screenshot.  
2. AI displays:  
   > “This is a delivery confirmation. You likely saved it to track a package.”  
3. Suggested action appears: **Add to calendar**  
4. User taps **Yes**, a pre-filled calendar event is created  
5. User confirms → Done    

---

## 🏆 Key Features

- Recover forgotten screenshot intent  
- AI-powered explanation and suggestion  
- Optional calendar integration for actionable reminders  
- Minimalist, distraction-free interface  
- Cross-platform with shared logic

---
## Running the project
* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Build and Run Web Application

To build and run the development version of the web app, use the run configuration from the run widget
in your IDE's toolbar or run it directly from the terminal:
- for the Wasm target (faster, modern browsers):
  - on macOS/Linux
    ```shell
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    ```
  - on Windows
    ```shell
    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
    ```
- for the JS target (slower, supports older browsers):
  - on macOS/Linux
    ```shell
    ./gradlew :composeApp:jsBrowserDevelopmentRun
    ```
  - on Windows
    ```shell
    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
    ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.


---

## 🔧 Future Enhancements

- Multi-screenshot batch analysis  
- Integration with note-taking apps  
- Smart categorization and tags