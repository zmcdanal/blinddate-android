plugins {
    kotlin("jvm")
    alias(libs.plugins.protobuf)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(libs.protobuf.kotlin.lite)
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protoc.get()}"
    }
    generateProtoTasks {
        all().configureEach {
            builtins {
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
