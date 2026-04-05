plugins {
    alias(libs.plugins.android.application)

    // ✅ Add this line
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.solartracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.solartracker"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ✅ Firebase BoM (IMPORTANT)
    implementation(platform("com.google.firebase:firebase-bom:34.11.0"))

    // ✅ Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    // ✅ Firestore Database
    implementation("com.google.firebase:firebase-firestore")

    // ✅ Optional Analytics
    implementation("com.google.firebase:firebase-analytics")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}