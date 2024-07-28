@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.perfomax.data"
    compileSdk = libs.versions.compileSdk.get().toInt()



    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(":core:utils"))

    implementation(project(":data_api"))
    implementation(project(":domain_models"))
    implementation(project(":core:ui"))

    // datastore
    implementation(libs.datastore.preferences)

    // di
    implementation(libs.javax.inject)

    // room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Email work
//    implementation("com.sun.mail:android-mail:1.6.2")
//    implementation("com.sun.mail:android-activation:1.6.2")

//    implementation("com.sun.mail:javax.mail:1.6.2") {
//        exclude("java.awt")
//    }

    implementation("com.sun.mail:android-mail:1.6.2")
//    implementation("com.sun.activation:jakarta.activation:2.0.1")

}