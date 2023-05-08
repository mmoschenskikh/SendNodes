package ru.maxultra.sendnodes

import java.security.MessageDigest

object HashEvaluator {

    fun computeHash(message: String, algorithm: String = DEFAULT_DIGEST_ALGORITHM) =
        MessageDigest
            .getInstance(algorithm)
            .digest(message.toByteArray())
            .fold(StringBuilder()) { sb, byte -> sb.append("%02x".format(byte)) }
            .toString()

    private const val DEFAULT_DIGEST_ALGORITHM = "SHA-256"
}
