plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id(Plugins.ksp)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api(project(":data"))
    implementation(project(":domain"))
    implementation(Deps.moshi)
    ksp(Deps.moshiCodegen)
    implementation(Deps.retrofit)
}
