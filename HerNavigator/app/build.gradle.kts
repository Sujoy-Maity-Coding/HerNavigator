plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlinx-serialization")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    id ("kotlin-kapt")
}

android {
    namespace = "com.sujoy.hernavigator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sujoy.hernavigator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // For gemini api
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    // For navigation and serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("androidx.navigation:navigation-compose:2.8.6")
    // For image loading
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1" )// Location Services
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.google.android.material:material:1.12.0")

    implementation("com.google.accompanist:accompanist-pager:0.30.1")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.30.1")
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.30.0") // Latest version

    implementation ("androidx.security:security-crypto:1.1.0-alpha06")
    implementation ("io.coil-kt:coil-gif:2.1.0")

    // For Ads
    implementation("com.google.android.gms:play-services-ads:23.6.0")

    implementation ("androidx.datastore:datastore-preferences:1.1.2")

    //Stylish bottom bar
    implementation("com.exyte:animated-navigation-bar:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("androidx.navigation:navigation-compose:2.8.0-beta05")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("androidx.work:work-runtime-ktx:2.10.0")
    implementation("com.google.guava:guava:31.0.1-android")

}