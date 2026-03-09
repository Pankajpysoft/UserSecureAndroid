# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Room entities
-keep class com.usersecure.app.database.** { *; }

# Keep AdMob classes
-keep class com.google.android.gms.ads.** { *; }

# Keep data binding
-keep class com.usersecure.app.databinding.** { *; }

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile
