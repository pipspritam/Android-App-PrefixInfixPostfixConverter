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

# WorkManager & Room Database reflection rules
-keep class * extends androidx.room.RoomDatabase { <init>(); }
-keep class androidx.work.impl.WorkDatabase_Impl { <init>(); }
-keep class * extends androidx.work.Worker { <init>(android.content.Context, androidx.work.WorkerParameters); }
-keep class * extends androidx.work.ListenableWorker { public <init>(android.content.Context, androidx.work.WorkerParameters); }
-keep class androidx.work.WorkerParameters { *; }
-keep class androidx.work.impl.** { *; }
-dontwarn androidx.work.impl.**

# Strip unused URI adaptive bitmap decoding in IconCompat$Api23Impl.toIcon (vm.c)
# and eliminate non-downsampled BitmapFactory overloads from DEX bytecode
-assumevalues class androidx.core.graphics.drawable.IconCompat {
    java.io.InputStream getUriInputStream(android.content.Context) return null;
}

-assumenosideeffects class androidx.core.graphics.drawable.IconCompat {
    java.io.InputStream getUriInputStream(android.content.Context) return null;
}

-assumenosideeffects class android.graphics.BitmapFactory {
    public static android.graphics.Bitmap decodeStream(java.io.InputStream) return null;
    public static android.graphics.Bitmap decodeByteArray(byte[], int, int) return null;
}

