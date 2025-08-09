plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.lukelorusso.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }

        release {
            val proguardFolder = "../proguard/"

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                proguardFolder + "dto-rules.pro",
                proguardFolder + "model-rules.pro",
                proguardFolder + "okhttp-rules.pro",
                proguardFolder + "retrofit-rules.pro"
            )
        }

        testBuildType = "debug"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.bundles.data)
    testImplementation(libs.bundles.data.test)
}
