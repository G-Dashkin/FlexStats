plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.perfomax.flexstats"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.perfomax.flexstats"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))

    implementation(project(":data"))
    implementation(project(":data_api"))
    implementation(project(":domain_models"))

    implementation(project(":features:start"))
    implementation(project(":features:start_api"))
    implementation(project(":features:auth"))
    implementation(project(":features:auth_api"))
    implementation(project(":features:home"))
    implementation(project(":features:home_api"))
    implementation(project(":features:projects"))
    implementation(project(":features:projects_api"))
    implementation(project(":features:accounts"))
    implementation(project(":features:accounts_api"))

    implementation(libs.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.google.material)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}