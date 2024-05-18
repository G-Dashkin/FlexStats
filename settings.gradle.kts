pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FlexStats"

include(":app")
include(":core:ui")
include(":core:navigation")
include(":data")
include(":data_api")
include(":domain_models")
include(":features")
include(":features:home")
include(":features:home_api")
include(":features:accounts")
include(":features:accounts_api")


include(":core:utils")
