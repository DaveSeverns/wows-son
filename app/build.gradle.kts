plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sevdotdev.wowson"
    compileSdk = ConfigData.targetSdkVersion

    defaultConfig {
        applicationId = "com.sevdotdev.wowson"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = (ConfigData.versionCode)
        versionName = (ConfigData.versionName)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
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
        kotlinCompilerExtensionVersion = "1.2.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation(project(":data-restimpl"))
    implementation(Deps.coreKtx)
    implementation(Deps.lifecycleKtx)
    implementation(Deps.activityCompose)
    implementation(Deps.composeUi)
    implementation(Deps.composePreview)
    implementation(Deps.materialDesign)
    testImplementation(Deps.junit)
    androidTestImplementation(Deps.junitExt)
    androidTestImplementation(Deps.espresso)
    androidTestImplementation(Deps.composeUiTest)
    debugImplementation(Deps.debugTooling)
    debugImplementation(Deps.debugManifest)

    //hilt
    implementation(Deps.hilt)
    implementation(Deps.hiltNavCompose)
    kapt(Deps.hiltCompiler)

    implementation(Deps.retrofit)
    implementation(Deps.retrofitMoshi)

    implementation(Deps.coil)
    implementation(Deps.exoPlayer)

    //navigation
    implementation(Deps.composeDestinationCore)
    ksp(Deps.composeDestinationKsp)

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")
}