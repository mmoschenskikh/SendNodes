package ru.maxultra.sendnodes

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

object BlockGenerator {

    suspend fun mineBlock(index: Int, prevHash: String, data: String): Block = withContext(Dispatchers.Default) {
        val stableBlockInfo = index.toString() + prevHash + data
        var nonce = 0L

        var fullBlockInfo = stableBlockInfo + nonce
        var blockHash = HashEvaluator.computeHash(fullBlockInfo)

        while (isActive && blockHash.endsWith(VALID_BLOCK_SUFFIX).not()) {
            nonce++
            if (nonce == 0L) {
                throw RuntimeException(
                    "Unable to compute hash for the following block:\n" +
                        "index: $index\n" +
                        "prevHash: $prevHash\n" +
                        "data: $data"
                )
            }
            fullBlockInfo = stableBlockInfo + nonce
            blockHash = HashEvaluator.computeHash(fullBlockInfo)
        }
        if (isActive.not()) throw CancellationException()

        return@withContext Block(
            index = index,
            prevHash = prevHash,
            hash = blockHash,
            data = data,
            nonce = nonce,
        )
    }

    private const val VALID_BLOCK_SUFFIX = "0000"
}
