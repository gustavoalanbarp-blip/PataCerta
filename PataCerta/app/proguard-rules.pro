# Regras padrão do Android Studio. Ver:
# https://developer.android.com/studio/build/shrink-code

# Mantém os modelos usados pelo Gson/Retrofit (evita ofuscação de campos JSON)
-keep class com.patacerta.app.data.remote.model.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
