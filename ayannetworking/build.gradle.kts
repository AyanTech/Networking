plugins {
    alias(libs.plugins.android.library)
}
android {
    namespace = "ir.ayantech.ayannetworking"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    api(platform(libs.okhttp.bom))
    api(libs.okhttp3.okhttp)
    api(libs.okhttp3.logging.interceptor)
    api(libs.retrofit)
    api(libs.gson)
    api(libs.converter.gson)
}