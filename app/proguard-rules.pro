# HayiDev++ ProGuard Rules

# Keep annotations
-keepattributes *Annotation*

# Keep Gson serialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.hayidev.app.data.model.** { *; }

# Keep Firebase
-keep class com.google.firebase.** { *; }

# Keep Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep Kotlin
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# Keep Compose
-keep class androidx.compose.** { *; }

# Keep Coil
-keep class coil.** { *; }

# Keep Facebook
-keep class com.facebook.** { *; }

# Keep RongCloud
-keep class io.rong.** { *; }

# Keep WebRTC
-keep class org.webrtc.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
