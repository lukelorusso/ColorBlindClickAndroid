plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.compose.compiler.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
    id(libs.plugins.google.services.get().pluginId)
    id(libs.plugins.google.firebase.crashlytics.get().pluginId)
}

val appId = "com.lukelorusso.colorblindclick"
val appVersionName = properties["version"].toString()
val appVersionCode = versionCodeFrom(appVersionName)
val appMinSdk = libs.versions.android.minSdk.get().toInt()
val appCompileSdk = libs.versions.android.compileSdk.get().toInt()

println("appId: $appId\n" +
        "appVersionName: $appVersionName\n" +
        "appVersionCode: $appVersionCode\n" +
        "appMinSdk: $appMinSdk\n" +
        "appCompileSdk: $appCompileSdk\n"
)

android {
    namespace = "com.lukelorusso.presentation"
    compileSdk = appCompileSdk

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }

    defaultConfig {
        applicationId = appId
        versionName = appVersionName
        versionCode = appVersionCode
        minSdk = appMinSdk
        targetSdk = appCompileSdk
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

/**
 * Transform a versionName in a stable versionCode, where each version block can be in the range of 1..999
 *
 * Example:
 * 3.0.20
 * _20 =>       020 = _20 * 1
 * __0 =>    000    = __0 * 1000
 * __3 => 003       = __3 * 1000 * 1000
 *     =>   3000020 (the final versionCode)
 */
fun versionCodeFrom(versionName: String): Int {
    val versionBlocks = versionName.split(".").reversed() // 3.0.20 => [20, 0, 3]
    var versionCode = 0
    var position = 0
    var multiplier = 1
    while (position < versionBlocks.size) {
        val versionBlock = versionBlocks[position].toIntOrNull() ?: 1
        versionCode += multiplier * versionBlock.coerceIn(0..999)
        multiplier *= 1000 // add 3 zeros for the next block
        position++
    }
    return versionCode
}
