object Deps {
    val activityCompose by lazy { "androidx.activity:activity-compose:${Versions.composeActivity}" }
    val coil by lazy { "io.coil-kt:coil-compose:${Versions.coil}" }
    val composeDestinationCore by lazy { "io.github.raamcosta.compose-destinations:core:${Versions.composeDestinations}" }
    val composeDestinationKsp by lazy { "io.github.raamcosta.compose-destinations:ksp:${Versions.composeDestinations}" }
    val composeBom by lazy { "androidx.compose:compose-bom:${Versions.composeBom}" }
    val composeIcons by lazy { "androidx.compose.material:material-icons-core" }
    val composePreview by lazy { "androidx.compose.ui:ui-tooling-preview" }
    val composeUi by lazy { "androidx.compose.ui:ui" }
    val composeUiTest by lazy { "androidx.compose.ui:ui-test-junit4" }
    val coreKtx by lazy { "androidx.core:core-ktx:${Versions.coreKtx}" }
    val coroutines by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}" }
    val debugManifest by lazy { "androidx.compose.ui:ui-test-manifest" }
    val debugTooling by lazy { "androidx.compose.ui:ui-tooling" }
    val espresso by lazy { "androidx.test.espresso:espresso-core:${Versions.espresso}" }
    val exoPlayer by lazy { "com.google.android.exoplayer:exoplayer:${Versions.exoPlayer}" }
    val hilt by lazy { "com.google.dagger:hilt-android:${Versions.hilt}" }
    val hiltCompiler by lazy { "com.google.dagger:hilt-compiler:${Versions.hilt}" }
    val hiltNavCompose by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.androidHilt}" }
    val junit by lazy { "junit:junit:${Versions.junit}" }
    val junitExt by lazy { "androidx.test.ext:junit:${Versions.testExt}" }
    val leakCanary by lazy { "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}" }
    val lifecycleKtx by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtx}" }
    val materialDesign by lazy { "androidx.compose.material3:material3" }
    val moshi by lazy { "com.squareup.moshi:moshi-kotlin:${Versions.moshi}" }
    val moshiCodegen by lazy { "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}" }
    val retrofit by lazy { "com.squareup.retrofit2:retrofit:${Versions.retrofit}" }
    val retrofitMoshi by lazy { "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}" }
}

object Plugins {
    const val androidApp = "com.android.application"
    const val androidLib = "com.android.library"
    const val composeCompiler = "org.jetbrains.kotlin.plugin.compose"
    const val kotlinJvm = "org.jetbrains.kotlin.jvm"
    const val hiltAndroid = "com.google.dagger.hilt.android"
    const val ksp = "com.google.devtools.ksp"
}