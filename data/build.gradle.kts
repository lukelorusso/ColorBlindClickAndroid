plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
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
        val proguardFolder = "../proguard/"

        debug {
            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                proguardFolder + "dto-rules.pro",
                proguardFolder + "gson-rules.pro",
                proguardFolder + "model-rules.pro",
                proguardFolder + "okhttp-rules.pro",
                proguardFolder + "retrofitRules-rules.pro"
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
    implementation(project(":domain"))
    implementation(libs.bundles.data)
    testImplementation(libs.bundles.data.test)
}
