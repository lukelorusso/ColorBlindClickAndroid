plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.compose.compiler.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
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
        targetSdk = libs.versions.android.compileSdk.get().toInt()
        versionCode = properties["versionCode"].toString().toInt()
        versionName = properties["versionName"].toString()
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
                proguardFolder + "model-rules.pro",
                proguardFolder + "okhttp-rules.pro",
                proguardFolder + "retrofit-rules.pro"
            )
            buildConfigField("Boolean", enableAnalyticsTag, "true")
        }

        testBuildType = "debug"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    jvmToolchain(22)
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar", "*.jar"))))
    implementation(libs.bundles.presentation)
    testImplementation(libs.bundles.presentation.test)
}
