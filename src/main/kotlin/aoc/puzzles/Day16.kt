package aoc.puzzles

import aoc.*
import java.util.*

/**
 * @author Kris | 16/12/2021
 */
object Day16 : Puzzle<Packet, Long>(16) {
    private const val VERSION_SIZE = 3
    private const val TYPE_SIZE = 3
    private const val LENGTH_TYPE_SIZE = 1
    private const val LENGTH_TYPE_REMAINING_BITS = 0
    private const val LENGTH_TYPE_REMAINING_PACKETS = 1
    private const val LITERAL_PACKET_TYPE = 4
    private const val SUB_PACKETS_BITS_SIZE = 15
    private const val SUB_PACKETS_COUNT_SIZE = 11
    private const val LITERAL_PACKET_PORTION_SIZE = 4
    override fun List<String>.parse(): Packet = ReadOnlyBitBuffer(HexFormat.of().parseHex(single())).decodePacket()

    private fun ReadOnlyBitBuffer.decodePacket(): Packet {
        val version = readBits(VERSION_SIZE)
        val type = readBits(TYPE_SIZE)
        return if (type == LITERAL_PACKET_TYPE) {
            LiteralPacket(version, type, decodeLiteralPacketValue())
        } else {
            OperatorPacket(version, type, decodeOperatorSubPackets())
        }
    }

    private tailrec fun ReadOnlyBitBuffer.decodeLiteralPacketValue(prev: Long = 0): Long {
        val end = readBits(1) == 0
        val value = (prev shl LITERAL_PACKET_PORTION_SIZE) or readBits(LITERAL_PACKET_PORTION_SIZE).toLong()
        return if (end) value else decodeLiteralPacketValue(value)
    }

    private fun ReadOnlyBitBuffer.decodeOperatorSubPackets(): List<Packet> = buildList {
        when (readBits(LENGTH_TYPE_SIZE)) {
            LENGTH_TYPE_REMAINING_BITS -> {
                val subPacketsBitCount = readBits(SUB_PACKETS_BITS_SIZE)
                val stopAt = remaining() - subPacketsBitCount
                while (remaining() > stopAt) {
                    add(decodePacket())
                }
            }
            LENGTH_TYPE_REMAINING_PACKETS -> repeat(readBits(SUB_PACKETS_COUNT_SIZE)) {
                add(decodePacket())
            }
        }
    }

    override fun Packet.solvePartOne(): Long = versionSum
    override fun Packet.solvePartTwo(): Long = sumValue
}

sealed interface Packet {
    val version: Int
    val type: Int
    val versionSum: Long
    val sumValue: Long
}

private data class LiteralPacket(override val version: Int, override val type: Int, val value: Long) : Packet {
    override val versionSum: Long get() = version.toLong()
    override val sumValue: Long = value
}

private data class OperatorPacket(override val version: Int, override val type: Int, val subPackets: List<Packet>) : Packet {
    override val versionSum: Long get() = version + subPackets.sumOf(Packet::versionSum)
    override val sumValue: Long get() = subPackets.map(Packet::sumValue).let(singleSealedInstance<OperatorPacketType> { it.id == type }::invoke)
}

@Suppress("unused")
private sealed class OperatorPacketType(val id: Int) : (List<Long>) -> Long {
    protected fun List<Long>.requireTwoElements(): List<Long> = apply { require(size == 2) }
    object Sum : OperatorPacketType(0) {
        override fun invoke(subValues: List<Long>): Long = subValues.reduce(Long::plus)
    }
    object Times : OperatorPacketType(1) {
        override fun invoke(subValues: List<Long>): Long = subValues.reduce(Long::times)
    }
    object Min : OperatorPacketType(2) {
        override fun invoke(subValues: List<Long>): Long = subValues.requireMin()
    }
    object Max : OperatorPacketType(3) {
        override fun invoke(subValues: List<Long>): Long = subValues.requireMax()
    }
    object GreaterThan : OperatorPacketType(5) {
        override fun invoke(subValues: List<Long>): Long = subValues.requireTwoElements().let { if (it.first() > it.last()) 1 else 0 }
    }
    object LesserThan : OperatorPacketType(6) {
        override fun invoke(subValues: List<Long>): Long = subValues.requireTwoElements().let { if (it.first() < it.last()) 1 else 0 }
    }
    object EqualTo : OperatorPacketType(7) {
        override fun invoke(subValues: List<Long>): Long = subValues.requireTwoElements().let { if (it.first() == it.last()) 1 else 0 }
    }
}
