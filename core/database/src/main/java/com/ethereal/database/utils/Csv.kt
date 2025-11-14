package com.ethereal.database.utils

internal fun Set<String>.toCsv(): String =
    this
        .filter { it.isNotBlank() }
        .joinToString(separator = "|")

internal fun String.csvToSet(): Set<String> =
    if (isBlank()) {
        emptySet()
    } else {
        this.split('|')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()
    }
