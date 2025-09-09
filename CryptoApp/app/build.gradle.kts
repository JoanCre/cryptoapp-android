plugins {
    alias(libs.plugins.es.rudo.application.compose)
    alias(libs.plugins.es.rudo.compose.ui)
    alias(libs.plugins.es.rudo.dependency.injection)
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

/** Common Android configurations are already handled by the convention plugins.
 * If module-specific configuration is needed, for example, for app signing (signingConfigs),
 * add it here. */
android {
    namespace = "com.rudo.cryptoapp" // Replace with your actual namespace
}

/** The convention plugins (`es.rudo.*`) already provide:
 *   All necessary Jetpack Compose dependencies (UI, Material3, Navigation, Activity, etc.)
 *   Compose compiler plugin.
 *   For dependency injection Hilt and KSP.
 *   Kotlinx Serialization JSON.
 *
 * Add modules or other app-specific dependencies.
 */

dependencies {
    // Networking
    implementation(libs.bundles.networking)
    
    // Dependency Injection
    implementation(libs.bundles.dependencyInjection)
    ksp(libs.hilt.android.compiler)
    
    // Lifecycle & Coroutines
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.coroutines)
    
    // Compose
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)
}
