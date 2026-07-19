import org.gradle.api.publish.maven.MavenPublication

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")
}
android {
    namespace = "com.ayantech.networking"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

}

dependencies {
    implementation(libs.kotlin.stdlib)

    api(platform(libs.okhttp.bom))
    api(libs.okhttp3.okhttp)
    api(libs.okhttp3.logging.interceptor)
    api(libs.retrofit)
    api(libs.gson)
    api(libs.converter.gson)

    //V2 dependenies
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    api(libs.ktor.client.core)
    api(libs.ktor.client.okhttp)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.serialization.json)
    api(libs.ktor.client.logging)

    testImplementation(libs.junit)

}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.ayantech.networking"
            artifactId = "ayan-networking"
            version = "2.0.4"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Ayan Networking")
                description.set("Networking library for Ayan Android applications")
                url.set("https://github.com/AyanTech/Networking")
            }
        }
    }
}