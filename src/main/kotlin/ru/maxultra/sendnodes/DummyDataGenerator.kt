package ru.maxultra.sendnodes

import kotlin.random.Random

object DummyDataGenerator {

    fun buildRandomString(length: Int = DEFAULT_STRING_LENGTH) =
        CharArray(length) { Random.nextBits(UTF_16_BITS_COUNT).toChar() }.concatToString()

    private const val DEFAULT_STRING_LENGTH = 256
    private const val UTF_16_BITS_COUNT = 16
}
