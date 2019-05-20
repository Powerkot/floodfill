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
) : AlgIterator {

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
                stack.push(
                    Cell(
                        cell.heightIndex,
                        cell.widthIndex + 1,
                        image[cell.heightIndex][cell.widthIndex + 1]
                    )
                )
            }
            //Проверка соседа снизу
            if (cell.heightIndex + 1 < imgHeight && image[cell.heightIndex + 1][cell.widthIndex] != !cell.isShaded) {
                stack.push(
                    Cell(
                        cell.heightIndex + 1,
                        cell.widthIndex,
                        image[cell.heightIndex + 1][cell.widthIndex]
                    )
                )
            }
            //Проверка соседа слева
            if (cell.widthIndex - 1 >= 0 && image[cell.heightIndex][cell.widthIndex - 1] != !cell.isShaded) {
                stack.push(
                    Cell(
                        cell.heightIndex,
                        cell.widthIndex - 1,
                        image[cell.heightIndex][cell.widthIndex - 1]
                    )
                )
            }
            //Проверка соседа сверху
            if (cell.heightIndex - 1 >= 0 && image[cell.heightIndex - 1][cell.widthIndex] != !cell.isShaded) {
                stack.push(
                    Cell(
                        cell.heightIndex - 1,
                        cell.widthIndex,
                        image[cell.heightIndex - 1][cell.widthIndex]
                    )
                )
            }
            logicCallback.onSingleIterationCompleted()
        } else {
            logicCallback.onAllIterationsCompleted()
        }
    }
}