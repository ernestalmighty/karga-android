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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile


# Support design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-keep class androidx.appcompat.** { *; }
-keep class androidx.annotation.** { *; }
-keep class androidx.core.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.constraintlayout.** { *; }
-keep class androidx.webkit.** { *; }

-keep class androidx.navigation.** { *; }
-keep class com.squareup.retrofit2.** { *; }
-keep class org.koin.** { *; }
-keep class io.reactivex.rxjava2.** { *; }
-keep class com.jakewharton.timber.** { *; }
-keep class com.github.bumptech.glide.** { *; }
-keep class com.google.android.material.** { *; }


-keep class androidx.legacy.** { *; }
-keep class com.android.support.** { *; }
-keep class org.jsoup.** { *; }
-keep class com.google.android.** { *; }
-keep class com.github.chrisbanes.** { *; }

-keep class com.squareup.okhttp3.** { *; }
-keep class com.newrelic.** { *; }
-keep class com.google.** { *; }
-keep class proguard.canary.** { *; }

-keep class androidx.room.** { *; }

-keep class org.jetbrains.kotlin.** { *; }
-keep class org.jetbrains.kotlinx.** { *; }

-keep class com.squareup.retrofit2.** { *; }
-keep class com.google.gson.** { *; }
-keep class androidx.room.** { *; }

# file processing
-keep class commons-io.** { *; }
-keep class org.apache.commons.io.** { *; }

# network
-keep class io.reactivex.rxjava2.** { *; }
-keep class com.squareup.retrofit2.** { *; }

-keep class com.gayyedfam.grainsmartkarga.data.model.OrderGroup { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.OrderHistory { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.Product { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.ProductDetail { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.ProductDetailWithVariants { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.ProductOrder { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.ProductType { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.ProductVariantsWithOrders { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.ProductWithDetail { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.Profile { *; }
-keep class com.gayyedfam.grainsmartkarga.data.model.Store { *; }

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
# Needed by google-http-client to keep generic types and @Key annotations accessed via reflection
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}
# Needed just to be safe in terms of keeping Google API service model classes
-keep class com.google.api.services.*.model.*
-keep class com.google.api.client.**
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
# See https://groups.google.com/forum/#!topic/guava-discuss/YCZzeCiIVoI
-dontwarn com.google.common.collect.MinMaxPriorityQueue
-dontobfuscate
# Assume dependency libraries Just Work(TM)
-dontwarn com.google.android.youtube.**
-dontwarn com.google.android.analytics.**
-dontwarn com.google.common.**
# Don't discard Guava classes that raise warnings
-keep class com.google.common.collect.MapMakerInternalMap$ReferenceEntry
-keep class com.google.common.cache.LocalCache$ReferenceEntry
# Make sure that Google Analytics doesn't get removed
-keep class com.google.analytics.tracking.android.CampaignTrackingReceiver
## BEGIN -- Google Play Services proguard.txt
-keep class * extends java.util.ListResourceBundle {
    protected java.lang.Object[][] getContents();
}
# Keep SafeParcelable value, needed for reflection. This is required to support backwards
# compatibility of some classes.
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
# Keep the names of classes/members we need for client functionality.
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
# Needed for Parcelable/SafeParcelable Creators to not get stripped
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
## END -- Google Play Services proguard.txt
# Other settings
-keep class com.android.**
-keep class com.google.android.**
-keep class com.google.android.gms.**
-keep class com.google.android.gms.location.**
-keep class com.google.api.client.**
-keep class com.google.maps.android.**

-keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     !static !transient <fields>;
     !private <fields>;
     !private <methods>;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }

