plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.twodew.repository"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Room DB
    api(libs.room.runtime)
    ksp(libs.room.compiler)

    // Room KTX
    implementation(libs.room.ktx)

    // Room KSP
    ksp(libs.room.compiler)

    // Hilt
    api(libs.hilt.android)
    ksp(libs.hilt.compiler)


    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.core.ktx)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.robolectric) // For JVM testing)
    androidTestImplementation(libs.espresso.core)
}