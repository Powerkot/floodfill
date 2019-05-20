package com.project.algorithm.algorithms

import com.project.algorithm.LogicCallback
import com.project.algorithm.models.Cell
import java.util.*

internal class FourWayAlg(
    private val image: Array<BitSet>,
    private val imgWidth: Int,
    private val imgHeight: Int,
    startCell: Cell,
    private val logicCallback: LogicCallback
) : IAlgorithm {

    private val stack = Stack<Cell>()

    init {
        this.stack.add(startCell) //Записываем в стек элемент, по которому было совершено нажатие
    }

    override fun iteration() {
        if (stack.isNotEmpty()) {
            val cell = stack.pop()
            image[cell.heightIndex][cell.widthIndex] = !cell.isShaded
            //Проверка соседа справа
            if (cell.widthIndex + 1 < imgWidth && image[cell.heightIndex][cell.widthIndex + 1] != !cell.isShaded) {
                pushUniqueCell(cell.widthIndex + 1, cell.heightIndex)
            }
            //Проверка соседа снизу
            if (cell.heightIndex + 1 < imgHeight && image[cell.heightIndex + 1][cell.widthIndex] != !cell.isShaded) {
                pushUniqueCell(cell.widthIndex, cell.heightIndex + 1)
            }
            //Проверка соседа слева
            if (cell.widthIndex - 1 >= 0 && image[cell.heightIndex][cell.widthIndex - 1] != !cell.isShaded) {
                pushUniqueCell(cell.widthIndex - 1, cell.heightIndex)
            }
            //Проверка соседа сверху
            if (cell.heightIndex - 1 >= 0 && image[cell.heightIndex - 1][cell.widthIndex] != !cell.isShaded) {
                pushUniqueCell(cell.widthIndex, cell.heightIndex - 1)
            }
            logicCallback.onSingleIterationCompleted()
        } else {
            logicCallback.onAllIterationsCompleted()
        }
    }

    override fun pushUniqueCell(widthIndex: Int, heightIndex: Int) {
        if (isCollectionAlreadyContainThisCell(widthIndex, heightIndex).not()) {
            stack.push(
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
        val listIterator = stack.listIterator()
        while (listIterator.hasNext() && !isAlreadyContainThisCell) {
            val cell = listIterator.next()
            if (cell.widthIndex == widthIndex && cell.heightIndex == heightIndex) {
                isAlreadyContainThisCell = true
            }
        }
        return isAlreadyContainThisCell
    }
}