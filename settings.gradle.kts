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
include(":core:utils")
include(":core:navigation")
include(":data")
include(":data_api")
include(":domain_models")

include(":features:start")
include(":features:start_api")
include(":features:auth")
include(":features:auth_api")
include(":features:home")
include(":features:home_api")
include(":features:projects")
include(":features:projects_api")
include(":features:accounts")
include(":features:accounts_api")