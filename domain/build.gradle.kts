plugins {
    id("kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.bundles.domain)
}
