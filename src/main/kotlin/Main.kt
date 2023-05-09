import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import ru.maxultra.sendnodes.Address
import ru.maxultra.sendnodes.LocalBlockChain
import ru.maxultra.sendnodes.Messenger
import ru.maxultra.sendnodes.Node
import ru.maxultra.sendnodes.getEnvOrThrow

fun main() = runBlocking {
    val address = getNodeAddress()
    val peers = getPeerAddresses()
    println("Address: $address, peers: $peers")
    val scope = CoroutineScope(SupervisorJob())
    val messenger = Messenger(
        address = address,
        scope = scope,
    )
    val node = Node(
        address = address,
        scope = scope,
        blockChain = LocalBlockChain(),
        peers = peers,
        messenger = messenger,
    )
    messenger.messageHandler = node
    node.run()
}

private fun getNodeAddress() = getEnvOrThrow("NODE_ADDRESS").asAddress()

private fun getPeerAddresses() = getEnvOrThrow("PEER_ADDRESSES").split(",").map(String::asAddress)

private fun String.asAddress(): Address {
    val parts = split(":")
    return when (parts.size) {
        1 -> Address(hostName = parts[0])
        2 -> Address(hostName = parts[0], port = parts[1].toInt())
        else -> throw IllegalArgumentException("Cannot parse socket address: $this")
    }
}
