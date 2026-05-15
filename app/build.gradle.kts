plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.kreedaankana"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kreedaankana"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("androidx.activity:activity-compose:1.8.2")

    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.compose.material3:material3")

    implementation("com.google.firebase:firebase-database-ktx:20.3.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
}

apply(plugin = "com.google.gms.google-services")
