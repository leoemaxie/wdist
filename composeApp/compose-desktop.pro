# Suppress warnings for Android-specific classes that are not used in desktop builds
-dontwarn android.**
-dontwarn androidx.**
-dontwarn dalvik.**

# Suppress warnings for optional dependencies
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn org.bouncycastle.**

# OkHttp platform-specific classes
-dontwarn okhttp3.internal.platform.**

# Keep main entry point
-keep class org.wdist.com.MainKt { *; }

# Keep all Compose-related classes
-keep class androidx.compose.** { *; }

# Ktor client rules
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { *; }

# Kotlinx serialization
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.** { *; }

# Keep data classes used for serialization
-keep @kotlinx.serialization.Serializable class * { *; }
