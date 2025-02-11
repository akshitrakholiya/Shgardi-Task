plugins {
    kotlin("kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.akshit.shgardi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.akshit.shgardi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug{
            buildConfigField("String", "AUTHORIZATION_VALUE", "\"Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMzE0MzI4MjU2OGQ5MWRiMzkxNTM4MmE4ZTg3MzdkYiIsIm5iZiI6MTczOTI3NTUzMy4yMzcsInN1YiI6IjY3YWIzZDBkNmViMzZkMGRhZDliYTZhNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.-iwH2bcBMdViCpIukS1z_sAuMdFFBYx6YUatHngv7Yw\"")
        }
        release {
            buildConfigField("String", "AUTHORIZATION_VALUE", "\"Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhMzE0MzI4MjU2OGQ5MWRiMzkxNTM4MmE4ZTg3MzdkYiIsIm5iZiI6MTczOTI3NTUzMy4yMzcsInN1YiI6IjY3YWIzZDBkNmViMzZkMGRhZDliYTZhNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.-iwH2bcBMdViCpIukS1z_sAuMdFFBYx6YUatHngv7Yw\"")

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
    dataBinding{
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    //Hilt-Dagger
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    //Lifecycle-ViewModel
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.livedata)

    //Retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2.converter.scalars)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.okhttp3.logging.interceptor)

    //Glide
    implementation(libs.glide.glide)
    implementation(libs.glide.compiler)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}