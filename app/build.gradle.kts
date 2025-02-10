plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.nedrysystems.joiefull"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nedrysystems.joiefull"
        minSdk = 21
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
    implementation("androidx.compose.foundation:foundation-layout-android:1.7.4")
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.analytics.impl)
    implementation(libs.androidx.runtime.livedata)

    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Retrofit + Moshi
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0" )
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")


    //Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    // Hilt dependencies
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")





    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Kotlin version
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")

    // Jetpack Compose
    implementation ("androidx.compose.ui:ui:1.5.0")
    implementation ("androidx.compose.material:material:1.5.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation ("androidx.activity:activity-compose:1.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation ("androidx.navigation:navigation-compose:2.6.0")

    // Pour les annotations de Compose
    implementation ("androidx.compose.runtime:runtime:1.5.0")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.fragment:fragment-ktx:1.8.4")
    implementation("androidx.databinding:databinding-runtime:7.1.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")
    implementation ("androidx.core:core-ktx:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation(kotlin("script-runtime"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    implementation(kotlin("test"))
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}
kapt {
    correctErrorTypes = true
}