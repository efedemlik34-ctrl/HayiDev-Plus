# HayiDev++ ProGuard Rules

# Firebase
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**

# RongCloud
-keep class io.rong.** { *; }
-dontwarn io.rong.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# Retrofit + Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }
-keep class com.hayidev.app.data.model.** { *; }
-keep class com.hayidev.app.data.remote.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keepclassmembers class * { @com.google.gson.annotations.SerializedName <fields>; }

# Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# Coil
-dontwarn coil.**
-keep class coil.** { *; }

# Lottie
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# WebRTC
-keep class org.webrtc.** { *; }
-dontwarn org.webrtc.**

# Billing
-keep class com.android.vending.billing.**
-keep class com.android.billingclient.** { *; }

# Facebook
-keep class com.facebook.** { *; }
-dontwarn com.facebook.**

# Google Play Services
-keep class com.google.android.gms.auth.** { *; }
-keep class com.google.android.gms.games.** { *; }
-keep class com.google.android.gms.dynamic.** { *; }

# App models
-keep class com.hayidev.app.data.model.** { *; }

# General
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-dontwarn javax.annotation.**
-keep class * extends java.util.ListResourceBundle { protected Object[][] getContents(); }
