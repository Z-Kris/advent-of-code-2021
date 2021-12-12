package aoc.day12

import aoc.Puzzle

/**
 * @author Kris | 12/12/2021
 */
object Day12 : Puzzle<NodeTree, Int>(12) {
    private const val START_LABEL = "start"
    private const val END_LABEL = "end"
    private const val SUCCESSFUL_PATH = 1
    private const val FAILED_PATH = 0

    @OptIn(ExperimentalStdlibApi::class)
    override fun Sequence<String>.parse(): NodeTree = buildMap<Node, MutableList<Node>> {
        this@parse.forEach {
            val (fromLabel, toLabel) = it.split('-')
            val from = Node(fromLabel, fromLabel.nodeType)
            val to = Node(toLabel, toLabel.nodeType)
            if (isValid(from, to)) getOrPut(from, ::mutableListOf).add(to)
            if (isValid(to, from)) getOrPut(to, ::mutableListOf).add(from)
        }
    }

    private fun isValid(from: Node, to: Node) = from.type != NodeType.End && to.type != NodeType.Start

    private val String.nodeType get() = when {
        this == START_LABEL -> NodeType.Start
        this == END_LABEL -> NodeType.End
        this.first().isLowerCase() -> NodeType.Small
        else -> NodeType.Large
    }

    private val NodeTree.start get() = keys.single { it.type == NodeType.Start }

    private fun NodeTree.visit(
        node: Node,
        path: Path,
        allowExtraSmall: Boolean
    ): Int {
        /* If we've reached the end, return here. */
        if (node.type == NodeType.End) return SUCCESSFUL_PATH
        /* If there are no more connected nodes to this node, return here. */
        val connectedNodes = get(node) ?: return FAILED_PATH
        /* Create a new path that's the continuation of previous, plus this node. */
        val nextPath = Path(path + node)
        /* Determine if we can allow a small node to get visited twice the next time around. */
        val nextAllowExtraSmall = if (allowExtraSmall) node.type != NodeType.Small || node !in path else false
        /* Now, compute the sum of all the remaining possible paths from this path onward. */
        return connectedNodes.filterNot { it.type == NodeType.Small && !nextAllowExtraSmall && it in path }.sumOf { nextNode ->
            visit(nextNode, nextPath, nextAllowExtraSmall)
        }
    }

    override fun NodeTree.solvePartOne(): Int = visit(start, Path(), false)
    override fun NodeTree.solvePartTwo(): Int = visit(start, Path(), true)
}
private typealias NodeTree = Map<Node, List<Node>>
data class Node(val name: String, val type: NodeType)
data class Path(val nodes: List<Node> = emptyList()) : List<Node> by nodes
enum class NodeType {
    Start,
    Large,
    Small,
    End,
}
