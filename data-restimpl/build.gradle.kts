plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(project(":data"))
    implementation(project(":domain"))
    implementation(Deps.moshi)
    kapt(Deps.moshiCodegen)
    implementation(Deps.retrofit)
}