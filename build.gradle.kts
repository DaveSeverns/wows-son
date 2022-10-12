// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.androidApp) version Versions.aGP apply false
    id (Plugins.androidLib) version Versions.aGP apply false
    id (Plugins.kotlinAndroid) version Versions.kotlin apply false
    id (Plugins.hiltAndroid) version Versions.hiltAndroidPlugin apply false
    id(Plugins.kotlinJvm) version Versions.kotlin apply false
}