plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lukelorusso.colorblindclick.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("int", "ROOM_VERSION", properties["roomDbVersion"].toString())
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

kotlin {
    jvmToolchain(22)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.bundles.data)
    ksp(libs.bundles.data.ksp)
    testImplementation(libs.bundles.data.test)
}
