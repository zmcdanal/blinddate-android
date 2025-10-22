plugins {
    kotlin("jvm")
    alias(libs.plugins.protobuf)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    // Kotlin FULL runtime (brings in protobuf-java transitively)
    api(libs.protobuf.kotlin)
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:${libs.versions.protoc.get()}" }

    generateProtoTasks {
        all().configureEach {
            builtins {
                // Keep Java (FULL) – do NOT set lite
                named("java").configure { /* no options */ }

                // Also generate Kotlin wrappers
                // (register if missing; named("kotlin") may exist already on newer plugin)
                // Safe approach:
                register("kotlin")
            }
        }
    }
}
