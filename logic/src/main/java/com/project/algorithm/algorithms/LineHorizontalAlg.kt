package com.project.algorithm.algorithms

import com.project.algorithm.LogicCallback
import com.project.algorithm.models.Cell
import java.util.*

internal class LineHorizontalAlg(
    private val image: Array<BitSet>,
    private val imgWidth: Int,
    private val imgHeight: Int,
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
            val y = cell.heightIndex
            var x = cell.widthIndex
            while (x > 0 && image[y][x - 1] == cell.isShaded) {
                x--

            }
            var spanUp = false
            var spanDown = false
            while (x < imgWidth && image[y][x] == cell.isShaded) {
                image[y][x] = !cell.isShaded
                if (!spanUp && y > 0 && image[y - 1][x] == cell.isShaded) {
                    pushUniqueCell(x, y - 1)
                    spanUp = true
                } else if (spanUp && y > 0 && image[y - 1][x] != cell.isShaded) {
                    spanUp = false
                }
                if (!spanDown && y < imgHeight - 1 && image[y + 1][x] == cell.isShaded) {
                    pushUniqueCell(x, y + 1)
                    spanDown = true
                } else if (spanDown && y < imgHeight - 1 && image[y + 1][x] != cell.isShaded) {
                    spanDown = false
                }
                x++
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