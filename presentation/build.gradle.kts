plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.compose.compiler.get().pluginId)
    id(libs.plugins.google.services.get().pluginId)
    id(libs.plugins.google.firebase.crashlytics.get().pluginId)
}

android {
    namespace = "com.lukelorusso.presentation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }

    defaultConfig {
        applicationId = "com.lukelorusso.colorblindclick"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 60
        versionName = "3.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val enableAnalyticsTag = "ENABLE_ANALYTICS"
        val proguardFolder = "../proguard/"

        debug {
            isShrinkResources = false
            isMinifyEnabled = false
            buildConfigField("Boolean", enableAnalyticsTag, "false")
        }

        release {
            isShrinkResources = true
            isMinifyEnabled = true
            buildConfigField("Boolean", enableAnalyticsTag, "true")
            proguardFiles(
                proguardFolder + "gson-rules.pro",
                proguardFolder + "model-rules.pro"
            )
        }

        testBuildType = "debug"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(libs.bundles.presentation)
    testImplementation(libs.bundles.presentation.test)
}
