pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "BlindDate"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":app",
    ":core:model",
    ":core:data",
    ":core:network",
    ":core:ui",
    ":features:home"
)

project(":core:model").projectDir = file("core/model")
project(":core:data").projectDir = file("core/data")
project(":core:network").projectDir = file("core/network")
project(":core:ui").projectDir = file("core/ui")
project(":features:home").projectDir = file("features/home")
include(":core:design")
include(":features:login")
include(":features:onboarding")
