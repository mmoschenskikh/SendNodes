import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import ru.maxultra.sendnodes.Address
import ru.maxultra.sendnodes.LocalBlockChain
import ru.maxultra.sendnodes.Messenger
import ru.maxultra.sendnodes.Node

fun main(args: Array<String>) = runBlocking {
    val address = args[0].split(":").run { Address(get(0), get(1).toInt()) }
    val peers = args[1].split(",").map {
        it.split(":").run { Address(get(0), get(1).toInt()) }
    }
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
