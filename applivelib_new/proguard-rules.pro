# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Linn/Care/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
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

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-dontskipnonpubliclibraryclasses #指定不去忽略非公共的库的类的成员
-printmapping mapping.txt

-keepattributes SourceFile
-keepattributes LineNumberTable #抛出异常时保留代码行号

-keepclasseswithmembernames class * { #保留所有本地的Native方法不被混淆
    native <methods>;
}

# 保留继承自Activity、Application这些类的子类
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.IntentService
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep class org.json.** { *; }

-dontwarn android.support.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep class android.support.v13.** { *; }
-keep class com.android.vending.billing.**
-keep class android.support.multidex.* {*;}
-keep public class com.tuniu.app.ui.fragment.** { *; }

#保留在Activity中的方法参数是view的方法，保证click事件正常
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#枚举类不能被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保留自定义控件不被混淆
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
   public <init>(android.content.Context);
   public <init>(android.content.Context, android.util.AttributesSet);
   public <init>(android.content.Context, android.util.AttributesSet, int);
}

#保留Parcelable序列号的类不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable{
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#对于R（资源）下的所有类及其方法，都不能被混淆
-keepattributes InnerClasses
-keep class **.R
-keep class **.R$* {
    <fields>;
}

#不混淆回调参数为onXXEvent
-keepclassmembers class * {
    void *(**on*Event);
}

#--------------------------Jar START-------------------
-keep class com.duapps.ad.** { *; }
-keep class	com.dianxinos.DXStatService.stat.TokenManager {
       public static java.lang.String getToken(android.content.Context);
}
-keep interface com.duapps.ad.** {*;}

-keep class com.nineoldandroids.** { *; }

-keep class com.dianxinos.lockscreen.** { *; }

-keep class com.nostra13.universalimageloader.** { *;}

-keep class com.xinmei365.** { *; }

# 高德 START
-keep class com.amap.api.location.**{ *; }
-keep class com.amap.api.fence.**{ *; }
-keep class com.autonavi.aps.amapapi.model.**{ *; }
# 高德 END

-keep class com.google.android.apps.dashclock.api.** { *; }

#友盟 START
-dontwarn com.umeng.**
-dontwarn org.apache.commons.**
-keep class com.umeng*.** { *; }
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#友盟 END

#Glide START
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#Glide END

#Volley START
-keep class com.android.volley.** { *; }
-keep class com.android.volley.toolbox.** { *; }
-keep class com.android.volley.Response$* { *; }

-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
#Volley END
#---------------------------jar END-------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, java.lang.String);
}
#----------------------------------------------------------------------------

#--------------------weather model start---------------

-dontwarn it.sephiroth.android.library.**
-keep class it.sephiroth.android.library.** { *; }

-dontwarn com.infolife.view.**
-keep class com.infolife.view.** { *; }

-dontwarn  com.handmark.pulltorefresh.library.**
-keep class com.handmark.pulltorefresh.library.** { *; }

-dontwarn com.service.chenwei.supportlibrary.**
-keep class com.service.chenwei.supportlibrary.** { *; }

-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** {*;}

#--------------------weather model end-----------------

#--------------------Third party aar start-------------------
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-dontwarn com.poliveira.apps.parallaxlistview.**
-keep class com.poliveira.apps.parallaxlistview.** { *; }
#--------------------Third party aar end-------------------

#--------------------不混淆LinearLayout的子类 start-------------
-keepclassmembers public class * extends android.widget.LinearLayout{
   void set*(***);
   *** get*();
   public <init>(android.content.Context, java.lang.String);
   public <init>(android.content.Context, android.util.AttributesSet);
   public <init>(android.content.Context, android.util.AttributesSet, int);
}

-keep public class * extends android.widget.Gallery
-keepclassmembernames public class mobi.infolife.ezweather.view.fancycoverflow.FancyCoverFlow {
    void set*(***);
   *** get*();
   public <init>(android.content.Context);
   public <init>(android.content.Context, android.util.AttributesSet);
   public <init>(android.content.Context, android.util.AttributesSet, int);
}

-keep public class * extends mobi.infolife.card.AmberCardView
-keepclassmembers public class * extends mobi.infolife.card.AmberCardView {
   void set*(***);
   *** get*();
   public <init>(android.content.Context, java.lang.String);
   public <init>(android.content.Context, android.util.AttributesSet);
   public <init>(android.content.Context, android.util.AttributesSet, int);
}
#--------------------不混淆CardView的子类 end-------------

#----------------------Iab start-----------------------
-keep class mobi.infolife.iab.** { *; }
#----------------------Iab end-------------------------

#----------------------Widget Start--------------------
-keep class * extends android.appwidget.AppWidgetProvider
#----------------------Widget End----------------------

#----------------js start------------------------------
-keepclassmembers class com.ljd.example.JSInterface {
    <methods>;
}
#----------------js end------------------------------ --

#--------------------reflex class start----------------
-keep class mobi.infolife.datasource.WeatherDataFetcher { *; }
-keep class com.amber.weather.WidgetUtils { *; }
#-keepclasseswithmembers class mobi.infolife.ezweather.WidgetUtils
#--------------------reflex class end------------------

-keep class com.nostra13.universalimageloader.** { *;}

# Facebook
-keep class com.facebook.** {*;}
-keep interface com.facebook.** {*;}
-keep enum com.facebook.** {*;}
-keep class com.flurry.android.** {*;}
-keep interface com.flurry.android.** { *; }
-keep class com.inmobi.ads.** {*;}
-dontwarn com.facebook.**


-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keep class com.google.firebase.** {*;}
-keep class com.google.android.gms.internal.** {*;}
-dontwarn com.samsung.android.**
-keep class com.samsung.android.** { *;}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.** {*;}

-keepclassmembers class com.supersonicads.sdk.controller.SupersonicWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}

-keep public class com.google.ads.** {
   public *;
}

-keep public class com.google.exoplayer2.** {
   public *;
}
-dontwarn com.google.android.exoplayer2.**

-dontwarn org.greenrobot.greendao.database.**
-dontwarn org.greenrobot.greendao.rx.**
-keep class org.greenrobot.dao.** {*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

-keep class com.applovin.** { *;}
-keep interface com.applovin.** {*;}
-dontwarn com.applovin.**

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, java.lang.String);
}


# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

#-libraryjars libs/PingStart_v3.1.2.jar
-keep class com.pingstart.adsdk.** {*;}
-keep interface com.pingstart.adsdk.** {*;}
-dontwarn com.pingstart.adsdk.**

-keep class com.amberweather.ist_library.** {*;}
-keepclassmembernames class com.amberweather.ist_library.** {*;}

-keep class com.bulletnoid.android.widget.** {*;}
-keep class com.handmark,pulltorefresh.library.** {*;}

-keep class mobi.infolife.ezweather.oldwidget.** {*;}
-keepclassmembernames class mobi.infolife.ezweather.oldwidget.** {*;}
-keep class mobi.infolife.ezweather.widgetscommon.WidgetView {*;}
-keepclassmembernames class mobi.infolife.ezweather.widgetcommon.** {*;}

-keep class mobi.infolife.ezweather.widgetscommon.** {*;}

-keep class mobi.infolife.ezweather.view.fancycoverflow.** {*;}

-keep public class mobi.infolife.ads.ApplySuccessActivity {*;}

-keep class mobi.infolife.card.view.style.** {*;}

-keepclassmembers class **.R$* {
     public static <fields>;
}
-keep class **.R {*;}
-keep class **.R$* {*;}
-keep class **.R$*
-keep class **.R
-dontwarn com.appsflyer.AFKeystoreWrapper

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mobvista.** {*; }
-keep interface com.mobvista.** {*; }
-keep class android.support.v4.** { *; }
-dontwarn com.mobvista.**
-keep class **.R$* { public static final int mobvista*; }

-dontwarn com.avocarrot.**
-keep class com.avocarrot.** { *; }
-keepclassmembers class com.avocarrot.** { *; }
-keep public class * extends android.view.View {
  public <init> (android.content.Context);
  public <init> (android.content.Context, android.util.AttributeSet);
  public <init> (android.content.Context, android.util.AttributeSet, int);
  public void set*(...);
}

-dontwarn org.apache.**
-keep class org.apache.** {*;}

-dontwarn android.provider.**
-keep class android.provider.** {*;}

-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**

-dontwarn
-keep class org.apache.cordova.** {*;}
-keep class org.apache.cordova.PluginManager {*;}
-keep class mobi.infolife.active.WhitelistPlugin {*;}

-dontwarn android.support.v8.renderscript.**
-keep public class android.support.v8.renderscript.** { *; }

-ignorewarnings

-keep class * {
    public private *;
}