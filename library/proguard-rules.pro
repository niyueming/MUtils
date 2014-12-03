# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontskipnonpubliclibraryclassmembers
-dontshrink
-dontoptimize
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-dontusemixedcaseclassnames
-keepattributes Exceptions,InnerClasses,Signature,*Annotation*,SourceFile,LineNumberTable
-dontpreverify
-verbose
-dontwarn android.**,android.support.v4.**,org.apache.http.**,android.webkit.WebView,**.R$*,com.nostra13.universalimageloader.**

-keep class android.support.v4.** {
    <fields>;
    <methods>;
}

-keep class org.apache.http.** {
    <fields>;
    <methods>;
}

-keep public class * extends android.support.**

-keep public class * extends org.xmlpull.**

-keep public class * extends android.app.Fragment

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper

-keep public class * extends android.preference.Preference

-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
   public void *(int);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
    <fields>;
    <methods>;
}

-keep class **.R {
    <fields>;
    <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

#不混淆内部类
#-keep class net.duiduipeng.ddp.Game$* {
#    public <fields>;
#    public <methods>;
#}
#-keep class net.duiduipeng.ddp.LuckDraw$* {
#    public <fields>;
#    public <methods>;
#}
#-keep class net.duiduipeng.ddp.CouponsList$* {
#    public <fields>;
#    public <methods>;
#}
#-keep class net.duiduipeng.ddp.ActionDetails$* {
#    public <fields>;
#    public <methods>;
#}