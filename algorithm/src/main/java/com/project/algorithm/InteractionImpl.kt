package com.project.algorithm

import java.util.*

class InteractionImpl : Interaction {

    private lateinit var image: Array<BitSet>
    private var imgSizeX = -1
    private var imgSizeY = -1
    private var startX = -1
    private var startY = -1

    private lateinit var algorithmType: AlgorithmType
    private lateinit var logicCallback: LogicCallback

    private val stack = Stack<Cell>()

    override fun generate(imgSizeX: Int, imgSizeY: Int) {
        this.imgSizeX = imgSizeX
        this.imgSizeY = imgSizeY
        this.image = Array(imgSizeY) {
            BitSet().apply {
                for (j in 0 until imgSizeX) {
                    set(j, false) //TODO временно все false для удобной отладки
//                    set(j, Math.random() <= 0.5)
                }
            }
        }
    }

    override fun init(image: Array<BitSet>) {

    }

    override fun start(x: Int, y: Int, algorithmType: AlgorithmType, logicCallback: LogicCallback) {
        this.startX = x
        this.startY = y
        this.algorithmType = algorithmType
        this.logicCallback = logicCallback
        this.stack.add(
            Cell(
                startX,
                startY,
                image[startX][startY]
            )
        ) //Записываем в стек элемент, по которому было совершено нажатие
        next() //выполнение первого шага
    }

    override fun next(): Array<BitSet> {
        when (algorithmType) {
            AlgorithmType.SIMPLE_FOUR_LINKED -> {
                if (stack.isNotEmpty()) {
                    val cell = stack.pop()
                    image[cell.x][cell.y] = !cell.isShaded
                    //Проверка соседа справа
                    if (cell.x + 1 < imgSizeX && image[cell.x + 1][cell.y] != !cell.isShaded) {
                        stack.push(Cell(cell.x + 1, cell.y, image[cell.x + 1][cell.y]))
                    }
                    //Проверка соседа снизу
                    if (cell.y + 1 < imgSizeY && image[cell.x][cell.y + 1] != !cell.isShaded) {
                        stack.push(Cell(cell.x, cell.y + 1, image[cell.x][cell.y + 1]))
                    }
                    //Проверка соседа слева
                    if (cell.x - 1 >= 0 && image[cell.x - 1][cell.y] != !cell.isShaded) {
                        stack.push(Cell(cell.x - 1, cell.y, image[cell.x - 1][cell.y]))
                    }
                    //Проверка соседа сверху
                    if (cell.y - 1 >= 0 && image[cell.x][cell.y - 1] != !cell.isShaded) {
                        stack.push(Cell(cell.x, cell.y - 1, image[cell.x][cell.y - 1]))
                    }
                    logicCallback.onCellChanged()
                } else {
                    logicCallback.onWorkCompleted()
                }
            }
            AlgorithmType.TWO -> {
                //TODO выполнение одного шага выбранного алгоритма
            }
            AlgorithmType.THREE -> {
                //TODO выполнение одного шага выбранного алгоритма
            }
        }
        return image
    }

    override fun getImage(): Array<BitSet> = image
}

interface LogicCallback {
    fun onCellChanged()
    fun onWorkCompleted()
}