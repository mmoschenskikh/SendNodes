package ru.maxultra.sendnodes

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class Node(
    private val address: Address,
    private val peers: List<Address>,
    private val scope: CoroutineScope,
    private val blockChain: LocalBlockChain,
    private val messenger: Messenger,
) : MessageHandler {

    private var blockMiningJob: Job? = null

    suspend fun run() {
        while (true) {
            try {
                blockMiningJob?.join()
            } catch (e: CancellationException) {
                println("$address : `blockMiningJob?.join()` error")
            }
            startBlockMining()
        }
    }

    override fun onNewMessage(msg: Message) {
        if (msg.to != address) return /* ignore because of wrong message destination */
        when (msg) {
            is Message.BlockOffer -> onBlockOffer(msg)
            is Message.ChainRequest -> onChainRequest(msg)
            is Message.ChainResponse -> onChainResponse(msg)
        }
    }

    private fun onBlockOffer(msg: Message.BlockOffer) {
        cancelBlockMining()
        val blockAddSuccess = blockChain.add(msg.block)
        if (blockAddSuccess.not()) {
            if (msg.block.index > blockChain.tail.index) {
                messenger.sendMessage(
                    Message.ChainRequest(
                        from = address,
                        to = msg.from,
                    )
                )
                println("$address : full chain requested from ${msg.from} because of wrong offer")
            } else {
                messenger.sendMessage(
                    Message.ChainResponse(
                        chain = blockChain.blocks,
                        from = address,
                        to = msg.from,
                    )
                )
                println("$address : full chain sent to ${msg.from} because of wrong offer")
            }
        } else {
            println("$address : block #${msg.block.index} (${msg.block.hash.take(6)}) added from ${msg.from}")
        }
        startBlockMining()
    }

    private fun onChainRequest(msg: Message.ChainRequest) {
        messenger.sendMessage(
            Message.ChainResponse(
                chain = blockChain.blocks,
                from = address,
                to = msg.from,
            )
        )
        println("$address : full chain sent to ${msg.from} as a response")
    }

    private fun onChainResponse(msg: Message.ChainResponse) {
        cancelBlockMining()
        blockChain.replace(msg.chain)
        println("$address : full chain received from ${msg.from}")
        startBlockMining()
    }

    private fun generateSingleBlock(data: String) = scope.launch {
        val lastBlock = blockChain.tail
        val newBlock = BlockGenerator.mineBlock(lastBlock.index + 1, lastBlock.hash, data)
        val blockAddSuccess = blockChain.add(newBlock)
        if (blockAddSuccess) {
            peers.forEach { peerPort ->
                messenger.sendMessage(
                    Message.BlockOffer(
                        block = newBlock,
                        from = address,
                        to = peerPort,
                    )
                )
            }
            println("$address : block #${newBlock.index} (${newBlock.hash.take(6)}) mined")
        } else {
            messenger.sendMessage(
                Message.ChainRequest(
                    from = address,
                    to = peers.random(),
                )
            )
        }
    }

    private fun startBlockMining() {
        val job = blockMiningJob
        if (job == null || job.isCompleted) {
            blockMiningJob = generateSingleBlock(DummyDataGenerator.buildRandomString())
        }
    }

    private fun cancelBlockMining() {
        blockMiningJob?.cancel()
        blockMiningJob = null
    }
}
