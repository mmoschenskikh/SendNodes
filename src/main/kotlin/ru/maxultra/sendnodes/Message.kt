package ru.maxultra.sendnodes

import kotlinx.serialization.Serializable

@Serializable
sealed class Message {

    abstract val from: Address
    abstract val to: Address

    @Serializable
    data class BlockOffer(
        val block: Block,
        override val from: Address,
        override val to: Address,
    ) : Message()

    @Serializable
    data class ChainRequest(
        override val from: Address,
        override val to: Address,
    ) : Message()

    @Serializable
    data class ChainResponse(
        val chain: List<Block>,
        override val from: Address,
        override val to: Address,
    ) : Message()
}
