package aoc

import java.nio.ByteBuffer

/**
 * @author Kris | 16/12/2021
 */
class ReadOnlyBitBuffer(private val bytes: ByteArray) {
    private val buffer = ByteBuffer.wrap(bytes)
    private var bitReaderIndex = 0

    fun remaining(): Int = (bytes.size shl 3) - bitReaderIndex

    fun readBits(numBits: Int): Int {
        if (numBits < 0 || numBits > 32) throw IllegalStateException("Number of bits must remain between 0 and 32(inclusive).")
        var bitsLeftToRead = numBits
        var bytePos = bitReaderIndex shr 3
        var bitOffset = 8 - (bitReaderIndex and 7)
        var value = 0
        bitReaderIndex += bitsLeftToRead
        while (bitsLeftToRead > bitOffset) {
            value += (buffer.get(bytePos++).toInt() and bitMasks[bitOffset]) shl bitsLeftToRead - bitOffset
            bitsLeftToRead -= bitOffset
            bitOffset = 8
        }
        value += if (bitOffset == bitsLeftToRead) {
            buffer.get(bytePos).toInt() and bitMasks[bitOffset]
        } else {
            buffer.get(bytePos).toInt() shr bitOffset - bitsLeftToRead and bitMasks[bitsLeftToRead]
        }
        return value
    }

    companion object {
        private const val MAX_BITS = 32
        private val bitMasks = IntArray(MAX_BITS)

        init {
            for (i in 0 until MAX_BITS) {
                bitMasks[i] = (1 shl i) - 1
            }
        }
    }
}
