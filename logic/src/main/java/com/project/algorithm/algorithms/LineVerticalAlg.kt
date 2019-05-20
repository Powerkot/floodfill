package com.project.algorithm.algorithms

import com.project.algorithm.LogicCallback
import com.project.algorithm.models.Cell
import java.util.*

internal class LineVerticalAlg(
        private val image: Array<BitSet>,
        private val imgHeight: Int,
        private val imgWidth: Int,
        startCell: Cell,
        private val logicCallback: LogicCallback
) : IAlgorithm {

    private val queue = LinkedList<Cell>()

    init {
        this.queue.add(startCell) //Записываем в очередь элемент, по которому было совершено нажатие
    }

    override fun iteration() {
        val cell = queue.poll()
        if (cell == null) {
            logicCallback.onAllIterationsCompleted()
        } else {
            var y = cell.heightIndex
            val x = cell.widthIndex
            while (y > 0 && image[y - 1][x] == cell.isShaded) {
                y--

            }
            var spanLeft = false
            var spanRight = false
            while (y < imgHeight && image[y][x] == cell.isShaded) {
                image[y][x] = !cell.isShaded
                if (!spanLeft && x > 0 && image[y][x - 1] == cell.isShaded) {
                    pushUniqueCell(x - 1, y)
                    spanLeft = true
                } else if (spanLeft && x > 0 && image[y][x - 1] != cell.isShaded) {
                    spanLeft = false
                }
                if (!spanRight && x < imgWidth - 1 && image[y][x + 1] == cell.isShaded) {
                    pushUniqueCell(x + 1, y)
                    spanRight = true
                } else if (spanRight && x < imgWidth - 1 && image[y][x + 1] != cell.isShaded) {
                    spanRight = false
                }
                y++
            }
            logicCallback.onSingleIterationCompleted()
        }
    }

    override fun pushUniqueCell(widthIndex: Int, heightIndex: Int) {
        if (isCollectionAlreadyContainThisCell(widthIndex, heightIndex).not()) {
            queue.push(
                    Cell(
                            widthIndex,
                            heightIndex,
                            image[heightIndex][widthIndex]
                    )
            )
        }
    }

    override fun isCollectionAlreadyContainThisCell(widthIndex: Int, heightIndex: Int): Boolean {
        var isAlreadyContainThisCell = false
        val listIterator = queue.listIterator()
        while (listIterator.hasNext() && !isAlreadyContainThisCell) {
            val cell = listIterator.next()
            if (cell.widthIndex == widthIndex && cell.heightIndex == heightIndex) {
                isAlreadyContainThisCell = true
            }
        }
        return isAlreadyContainThisCell
    }
}