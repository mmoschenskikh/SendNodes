package ru.maxultra.sendnodes

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val hostName: String,
    val port: Int,
) {

    override fun toString(): String {
        return "$hostName:$port"
    }
}
