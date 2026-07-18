# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in getDefaultProguardFile('proguard-android-optimize.txt').

# Keep Kotlin serialization properties from being obfuscated/removed
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
