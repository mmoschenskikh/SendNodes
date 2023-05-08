package ru.maxultra.sendnodes

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Messenger(
    address: Address,
    private val scope: CoroutineScope,
    var messageHandler: MessageHandler? = null,
) {

    private val format = Json { classDiscriminator = "#class" }
    private val selectorManager = SelectorManager(Dispatchers.IO)

    init {
        val serverSocket = aSocket(selectorManager)
            .tcp()
            .bind(address.hostName, address.port)

        scope.launch(Dispatchers.IO) {
            while (true) {
                val socket = serverSocket.accept()
                try {
                    val readChannel = socket.openReadChannel()
                    val incomingArray = ByteArray(readChannel.readInt())
                    readChannel.readFully(incomingArray)
                    val jsonMessage = String(incomingArray)
                    messageHandler?.onNewMessage(format.decodeFromString(jsonMessage))
                } catch (e: Exception) {
                    println("${address.port} : message parsing failed")
                }
            }
        }
    }

    fun sendMessage(msg: Message) {
        scope.launch {
            val writeChannel = aSocket(selectorManager)
                .tcp()
                .connect(msg.to.hostName, msg.to.port)
                .openWriteChannel(autoFlush = true)
            val message = format.encodeToString(msg).toByteArray()
            writeChannel.writeInt(message.size)
            writeChannel.writeFully(message)
        }
    }
}
