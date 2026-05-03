-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keep class com.echohabit.app.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep interface dagger.hilt.** { *; }

# Room
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}

# Jetpack Compose
-keep class androidx.compose.** { *; }

# TensorFlow Lite
-keep class org.tensorflow.lite.** { *; }
