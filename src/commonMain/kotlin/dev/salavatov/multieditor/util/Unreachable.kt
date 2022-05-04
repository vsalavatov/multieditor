package dev.salavatov.multieditor.util

fun unreachable(msg: String? = null): Nothing =
    throw RuntimeException("unreachable${msg?.let { ": $it" } ?: ""}")