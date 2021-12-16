package aoc.day16

import aoc.day16.Day16.solvePartOne
import aoc.day16.Day16.solvePartTwo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 16/12/2021
 */
internal class Day16Test {

    @Test
    fun partOne() {
        assertEquals(6, Day16.parse("D2FE28").solvePartOne())
        assertEquals(9, Day16.parse("38006F45291200").solvePartOne())
        assertEquals(14, Day16.parse("EE00D40C823060").solvePartOne())
        assertEquals(16, Day16.parse("8A004A801A8002F478").solvePartOne())
        assertEquals(23, Day16.parse("C0015000016115A2E0802F182340").solvePartOne())
        assertEquals(31, Day16.parse("A0016C880162017C3686B18A3D4780").solvePartOne())
        assertEquals(854, Day16.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(3, Day16.parse("C200B40A82").solvePartTwo())
        assertEquals(54, Day16.parse("04005AC33890").solvePartTwo())
        assertEquals(7, Day16.parse("880086C3E88112").solvePartTwo())
        assertEquals(9, Day16.parse("CE00C43D881120").solvePartTwo())
        assertEquals(1, Day16.parse("D8005AC2A8F0").solvePartTwo())
        assertEquals(0, Day16.parse("F600BC2D8F").solvePartTwo())
        assertEquals(0, Day16.parse("9C005AC2F8F0").solvePartTwo())
        assertEquals(1, Day16.parse("9C0141080250320F1802104A08").solvePartTwo())
        assertEquals(186_189_840_660, Day16.parse().solvePartTwo())
    }
}
