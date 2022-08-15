object Deps {
    val activityCompose by lazy { "androidx.activity:activity-compose:${Versions.composeActivity}" }
    val composeUi by lazy { "androidx.compose.ui:ui:${Versions.compose}" }
    val composePreview by lazy { "androidx.compose.ui:ui-tooling-preview:${Versions.compose}" }
    val composeUiTest by lazy { "androidx.compose.ui:ui-test-junit4:${Versions.compose}" }
    val coreKtx by lazy { "androidx.core:core-ktx:${Versions.coreKtx}" }
    val coroutines by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}" }
    val debugManifest by lazy { "androidx.compose.ui:ui-test-manifest:${Versions.compose}" }
    val debugTooling by lazy { "androidx.compose.ui:ui-tooling:${Versions.compose}" }
    val espresso by lazy { "androidx.test.espresso:espresso-core:${Versions.espresso}" }
    val hilt by lazy { "com.google.dagger:hilt-android:${Versions.hilt}" }
    val hiltCompiler by lazy { "com.google.dagger:hilt-compiler:${Versions.hilt}" }
    val junit by lazy { "junit:junit:${Versions.junit}" }
    val junitExt by lazy { "androidx.test.ext:junit:${Versions.testExt}" }
    val lifecycleKtx by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtx}" }
    val materialDesign by lazy { "androidx.compose.material3:material3:${Versions.material3}" }
    val moshi by lazy { "com.squareup.moshi:moshi-kotlin:${Versions.moshi}" }
    val moshiCodegen by lazy { "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}" }
    val retrofit by lazy { "com.squareup.retrofit2:retrofit:${Versions.retrofit}" }
    val retrofitMoshi by lazy { "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}" }
}