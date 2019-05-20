package com.project.algorithm.algorithms

import com.project.algorithm.LogicCallback
import com.project.algorithm.models.Cell
import java.util.*

internal class TestAlg(
    private val image: Array<BitSet>,
    private val imgHeight: Int,
    private val imgWidth: Int,
    startCell: Cell,
    private val logicCallback: LogicCallback
) : AlgIterator {

    private val stack = Stack<Cell>()

    init {
        this.stack.add(startCell) //Записываем в очередь элемент, по которому было совершено нажатие
    }

    override fun iteration() {
        val node = stack.pop()
        if (node == null) {
            logicCallback.onAllIterationsCompleted()
        } else {
            //TODO
        }
    }
}