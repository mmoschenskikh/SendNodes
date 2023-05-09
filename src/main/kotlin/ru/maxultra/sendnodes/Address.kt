package ru.maxultra.sendnodes

import kotlinx.serialization.Serializable

private const val DEFAULT_PORT = 1337

@Serializable
data class Address(
    val hostName: String,
    val port: Int = DEFAULT_PORT,
) {

    override fun toString(): String {
        return "$hostName:$port"
    }
}
