# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# Preserve generic type signatures for Gson
-keepattributes Signature

# Keep all Gson fields and classes
-keep class com.google.gson.** { *; }

# Keep specific types used in TypeToken
-keep class com.music.mp3.spotify.mealcount.data.local.converter.Converters {
    <fields>;
    <methods>;
}

# Keep your entities and DAOs
-keep class com.music.mp3.spotify.mealcount.data.local.entity.** { *; }
-keep class com.music.mp3.spotify.mealcount.data.local.** { *; }
-keep interface androidx.room.Dao {*;}
