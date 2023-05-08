package ru.maxultra.sendnodes

import java.util.*

/**
 * Statically obtained initial block of the chain.
 */
private val genesisBlock = Block(
    index = 0,
    prevHash = "0000000000000000000000000000000000000000000000000000000000000000",
    hash = "8fe771e474dc42ad6e09683963e6442768bf5eb61f7d9213678db38c309b0000",
    data = "hello from genesis block",
    nonce = 66565,
)

class LocalBlockChain {

    private val _blocks = LinkedList<Block>()
    val blocks: List<Block>
        get() = _blocks

    val tail: Block
        get() = blocks.last()

    init {
        _blocks.add(genesisBlock)
    }

    fun add(block: Block): Boolean {
        val isAddingToTail = block.index == _blocks.size
        return isAddingToTail && block.isValid() && _blocks.add(block)
    }

    fun replace(newBlocks: List<Block>) {
        _blocks.clear()
        _blocks.addAll(newBlocks)
    }

    private fun Block.isValid(): Boolean {
        val blockInfo = index.toString() + prevHash + data + nonce
        val localHash = HashEvaluator.computeHash(blockInfo)

        return prevHash == _blocks[index - 1].hash && hash == localHash
    }
}
