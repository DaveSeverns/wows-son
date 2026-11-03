plugins {
    id(Plugins.androidApp)
    id(Plugins.composeCompiler)
    id(Plugins.hiltAndroid)
    id(Plugins.ksp)
    id(Plugins.kotlinSerialization)
}

android {
    namespace = "com.sevdotdev.wowson"
    compileSdk = ConfigData.compileSdkVersion

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    implementation(project(":data-restimpl"))
    implementation(project(":domain"))
    implementation(Deps.coreKtx)
    implementation(Deps.lifecycleKtx)
    implementation(Deps.activityCompose)
    implementation(platform(Deps.composeBom))
    implementation(Deps.composeUi)
    implementation(Deps.composePreview)
    implementation(Deps.materialDesign)
    implementation(Deps.composeIcons)
    testImplementation(Deps.junit)
    androidTestImplementation(Deps.junitExt)
    androidTestImplementation(Deps.espresso)
    androidTestImplementation(platform(Deps.composeBom))
    androidTestImplementation(Deps.composeUiTest)
    debugImplementation(Deps.debugTooling)
    debugImplementation(Deps.debugManifest)

    //hilt
    implementation(Deps.hilt)
    implementation(Deps.hiltNavCompose)
    ksp(Deps.hiltCompiler)

    implementation(Deps.retrofit)
    implementation(Deps.retrofitMoshi)

    implementation(Deps.coil)
    implementation(Deps.exoPlayer)

    //navigation
    implementation(Deps.navigation3Runtime)
    implementation(Deps.navigation3Ui)
    implementation(Deps.lifecycleViewModelNavigation3)
    implementation(Deps.kotlinxSerializationJson)

    debugImplementation(Deps.leakCanary)
}