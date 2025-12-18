# Previous rules
-keep class com.google.errorprone.annotations.** { *; }
-dontwarn com.google.errorprone.annotations.**
-keep class com.nimbusds.** { *; }
-keep class com.nimbusds.jose.shaded.** { *; }
-keep class com.google.gson.** { *; }

# Google Tink rules
-keep class com.google.crypto.tink.subtle.** { *; }
-dontwarn com.google.crypto.tink.subtle.**

# Bouncy Castle rules
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# Prevent removal of required providers
-keep class com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton { *; }
-keep class com.nimbusds.jose.jwk.PEMEncodedKeyParser { *; }
-keep class com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator { *; }
-keep class com.nimbusds.jose.crypto.Ed25519Signer { *; }
-keep class com.nimbusds.jose.crypto.Ed25519Verifier { *; }
-keep class com.nimbusds.jose.crypto.X25519Encrypter { *; }
-keep class com.nimbusds.jose.crypto.impl.XC20P { *; }