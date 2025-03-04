plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
/*    packagingOptions {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/DEPENDENCIES"
            )
        }
    }*/
    namespace = "com.example.mletask"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mletask"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.ui.test.junit4.android)
    implementation(libs.androidx.navigation.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Jetpack Compose
    implementation(libs.ui)
    //implementation(libs.androidx.material)
    implementation(libs.androidx.navigation.compose)

    // ViewModel & LiveData
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Dependency Injection (Hilt)
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit for API Calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room Database for caching
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Coil for Image Loading
    implementation(libs.coil.compose)

    implementation(libs.accompanist.pager)

    //Test
// Unit testing
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation(libs.kotlin.test) // Kotlin test framework
    testImplementation(libs.mockk.v1133) // MockK for mocking
    testImplementation(libs.kotlinx.coroutines.test.v160) // Coroutines testing

// Instrumented testing
    androidTestImplementation(libs.androidx.ui.test.junit4.v100) // Compose testing
    androidTestImplementation(libs.androidx.junit.ktx) // Android JUnit with Kotlin extensions
    androidTestImplementation(libs.androidx.espresso.core.v361) // Espresso
    androidTestImplementation(libs.mockk.android.v1133) // MockK for Android
}