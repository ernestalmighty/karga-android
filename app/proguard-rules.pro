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

