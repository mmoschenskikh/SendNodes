package ru.maxultra.sendnodes

fun getEnvOrThrow(name: String) =
    System.getenv(name)?.ifBlank { null } ?: throw NoSuchElementException("Environment variable $name was not found")
