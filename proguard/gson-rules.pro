-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep public class com.google.gson.** {public private protected *;}
-dontwarn sun.misc.**

# This is also needed for R8 in compat mode since multiple
# optimizations will remove the generic signature such as class
# merging and argument removal. See:
# https://r8.googlesource.com/r8/+/refs/heads/main/compatibility-faq.md#troubleshooting-gson-gson
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
