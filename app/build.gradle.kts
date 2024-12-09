plugins {
    id("com.android.application")
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.google.gms.google.services)
    id("com.google.gms.google-services")
//    id("com.google.gms.google-services") version "4.4.2" apply false
//    id("com.google.gms:google-services:4.4.2")
//    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.studybuddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.studybuddy"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {


//    implementation(platform("com.google.firebase:firebase-database:21.0.0"))
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-storage:21.0.1")
    implementation ("com.google.firebase:firebase-database:19.5.0")
    implementation ("com.google.firebase:firebase-auth:19.3.1")
    implementation ("com.google.firebase:firebase-auth:23.1.0")
//    implementation("com.google.firebase:firebase-analytics")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation ("androidx.test:runner:1.4.0")
    androidTestImplementation ("androidx.test:rules:1.4.0")

    androidTestImplementation(libs.espresso.core)
}
