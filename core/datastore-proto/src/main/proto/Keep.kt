/**
 * Tiny “keep” type to ensure this module produces Kotlin metadata.
 *
 * Why this exists:
 * - `:core:datastore-proto` only generates Java *lite* classes from `.proto` files.
 * - When a module has no Kotlin sources, the resulting JAR lacks
 *   `META-INF/<module>.kotlin_module`.
 * - KSP (used by other modules) scans classpath artifacts and expects that Kotlin
 *   metadata file to exist. Without it, KSP may fail with
 *   `.../datastore-proto.jar!/META-INF/datastore-proto.kotlin_module` not found.
 *
 * What this does:
 * - Providing a single Kotlin type forces the Kotlin compiler to emit the
 *   `.kotlin_module` metadata into the JAR, unblocking KSP.
 *
 * Notes:
 * - This type is `internal` and intentionally unused; it has no runtime impact.
 * - If you later add real Kotlin sources to this module (or switch to non-lite
 *   generation that includes Kotlin sources), you can delete this file.
 */
@file:Suppress("unused")

internal object _ProtoKeep