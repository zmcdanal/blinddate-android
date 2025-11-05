package com.ethereal.database.utils

internal fun List<String>.toCsv(): String = joinToString("|")
internal fun String.csvToList(): List<String> =
    if (isBlank()) emptyList() else split('|').map { it.trim() }.filter { it.isNotEmpty() }