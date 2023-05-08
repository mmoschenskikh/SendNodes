package ru.maxultra.sendnodes

interface MessageHandler {

    fun onNewMessage(msg: Message)
}
