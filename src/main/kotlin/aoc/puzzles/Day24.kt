package aoc.puzzles

import aoc.Vec4i
import aoc.chunkedBy
import aoc.singleSealedInstance
import kotlin.reflect.KProperty1

/**
 * @author Kris | 24/12/2021
 */
object Day24 : Puzzle<List<AluInstructions>, Long>(24) {
    private val DIGITS_RANGE = 1..9
    private val DECREMENTING_DIGITS_RANGE = DIGITS_RANGE.reversed()
    private const val DIGITS = 14

    /* Build a list of powers-of-10 values, so [1, 10, 100, 1000, etc] through string manipulation. */
    private val POWERS_OF_10_REVERSED = List(DIGITS) { zeroCount -> "1${ "0".repeat(zeroCount)}".toLong() }.reversed()

    /* All input seems to affect the 'w' variable of the vec4. */
    private const val INPUT_STRING = "inp w"

    /* Exclude inp instruction as we handle that separately, as a line break. */
    private val INSTRUCTION_REGEX = Regex("""(add|mul|div|mod|eql) ([xyz]) (-?\d+|[xyzw])""")

    override fun List<String>.parse(): List<AluInstructions> = drop(1).chunkedBy(INPUT_STRING::equals).map { list ->
        val instructions = list.map { line ->
            val match = INSTRUCTION_REGEX.matchEntire(line) ?: error("Corrupted line: $line")
            val (ins, fieldName, operandString) = match.destructured
            val operation = singleSealedInstance<AluOperation> { it.name == ins }
            val field = fieldName.single().toVec4iField
            val operandAsInt = operandString.toIntOrNull()
            if (operandAsInt != null) {
                AluLiteralInstruction(operation, field, operandAsInt)
            } else {
                val operandAsField = operandString.single().toVec4iField
                AluFieldInstruction(operation, field, operandAsField)
            }
        }
        AluInstructions(instructions)
    }

    private val Char.toVec4iField
        get() = when (this) {
            'x' -> Vec4i::x
            'y' -> Vec4i::y
            'z' -> Vec4i::z
            'w' -> Vec4i::w
            else -> error("Invalid field: $this")
        }

    override fun List<AluInstructions>.solvePartOne(): Long = requireNotNull(depthFirstSearch(DECREMENTING_DIGITS_RANGE)) { "Cannot find path." }
    override fun List<AluInstructions>.solvePartTwo(): Long = requireNotNull(depthFirstSearch(DIGITS_RANGE)) { "Cannot find path." }

    /**
     * Bitpacks a visited-node index based on the digit index and the 'z' value of the [Vec4i].
     */
    private fun Vec4i.visitIndex(digitIndex: Int) = (z shl 4) or digitIndex

    private fun List<AluInstructions>.depthFirstSearch(
        digitsRange: IntProgression,
        digitIndex: Int = 0,
        vector: Vec4i = Vec4i.ZERO,
        visited: MutableSet<Int> = mutableSetOf(),
    ): Long? {
        /* Check for arrival - the z variable must be zero by the time we reach the final digit. */
        if (digitIndex == DIGITS) {
            return if (vector.z == 0) 0 else null
        }
        /* Check if the node has already been visited, if not, mark it visited. */
        if (!visited.add(vector.visitIndex(digitIndex))) return null

        val instructions = get(digitIndex)
        return digitsRange.firstNotNullOfOrNull { digit ->
            val nextVector = instructions.fold(vector.copy(w = digit)) ?: return@firstNotNullOfOrNull null
            val trailingDigits = depthFirstSearch(digitsRange, digitIndex.inc(), nextVector, visited) ?: return@firstNotNullOfOrNull null
            trailingDigits + digit * POWERS_OF_10_REVERSED[digitIndex]
        }
    }

    /**
     * Folds the [vector] into the result by performing a set of mathematical operations on it.
     * Due to there being the division and modulo operations present, it is possible for the
     * folding process to fail and throw an [ArithmeticException].
     * If that happens, the resulting vector will be null.
     */
    private fun AluInstructions.fold(vector: Vec4i): Vec4i? = try {
        instructions.fold(vector) { acc, value ->
            value.getResult(acc)
        }
    } catch (e: ArithmeticException) {
        null
    }
}

data class AluInstructions(val instructions: List<AluInstruction<*>>) : List<AluInstruction<*>> by instructions
private typealias Vec4iField = KProperty1<Vec4i, Int>

interface AluInstruction<T> {
    val operation: AluOperation
    val leftOperand: Vec4iField
    val rightOperand: T
    fun Vec4i.getRightHandValue(): Int
    fun getResult(vector: Vec4i): Vec4i {
        val value = leftOperand.get(vector)
        val result = operation.invoke(value, vector.getRightHandValue())
        return vector.copy(leftOperand, result)
    }
}

data class AluLiteralInstruction(
    override val operation: AluOperation,
    override val leftOperand: Vec4iField,
    override val rightOperand: Int
) : AluInstruction<Int> {
    override fun Vec4i.getRightHandValue(): Int = rightOperand
}

data class AluFieldInstruction(
    override val operation: AluOperation,
    override val leftOperand: Vec4iField,
    override val rightOperand: Vec4iField
) : AluInstruction<Vec4iField> {
    override fun Vec4i.getRightHandValue(): Int = rightOperand.get(this)
}

@Suppress("unused")
sealed class AluOperation(val name: String) : (Int, Int) -> Int {
    object Add : AluOperation("add") {
        override fun invoke(a: Int, b: Int): Int = a + b
    }

    object Mul : AluOperation("mul") {
        override fun invoke(a: Int, b: Int): Int = a * b
    }

    object Div : AluOperation("div") {
        override fun invoke(a: Int, b: Int): Int {
            if (b == 0) throw ArithmeticException("Cannot divide by zero.")
            return a / b
        }
    }

    object Mod : AluOperation("mod") {
        override fun invoke(a: Int, b: Int): Int {
            if (a < 0 || b <= 0) throw ArithmeticException("Cannot acquire the remainder of the two values $a, $b")
            return a % b
        }
    }

    object Eql : AluOperation("eql") {
        override fun invoke(a: Int, b: Int): Int = if (a == b) 1 else 0
    }
}
