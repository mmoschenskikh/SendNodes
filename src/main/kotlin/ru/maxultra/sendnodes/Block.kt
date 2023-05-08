package ru.maxultra.sendnodes

import kotlinx.serialization.Serializable

@Serializable
data class Block(
    val index: Int,
    val prevHash: String,
    val hash: String = "",
    val data: String,
    val nonce: Long = 0,
)
