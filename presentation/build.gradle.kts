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
        versionCode = 62
        versionName = "3.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val enableAnalyticsTag = "ENABLE_ANALYTICS"

        debug {
            versionNameSuffix = ".debug"
            isMinifyEnabled = false
            buildConfigField("Boolean", enableAnalyticsTag, "false")
        }

        release {
            val proguardFolder = "../proguard/"

            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                proguardFolder + "dto-rules.pro",
                proguardFolder + "gson-rules.pro",
                proguardFolder + "model-rules.pro",
                proguardFolder + "okhttp-rules.pro",
                proguardFolder + "retrofit-rules.pro"
            )
            buildConfigField("Boolean", enableAnalyticsTag, "true")
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
