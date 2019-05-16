package com.project.algorithm

import java.util.*

class InteractionImpl : Interaction {

    private var testCounter = 0

    private lateinit var image: Array<BitSet>
    private var imgSizeX = -1
    private var imgSizeY = -1
    private var startX = -1
    private var startY = -1

    private lateinit var algorithmType: AlgorithmType
    private lateinit var logicCallback: LogicCallback

    override fun generate(imgSizeX: Int, imgSizeY: Int) {
        this.imgSizeX = imgSizeX
        this.imgSizeY = imgSizeY
        this.image = Array(imgSizeY) {
            BitSet().apply {
                for (j in 0 until imgSizeX) {
                    set(j, Math.random() <= 0.5)
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
        next() //выполнение первого шага
    }

    override fun next(): Array<BitSet> {
        when (algorithmType) {
            AlgorithmType.SIMPLE_FOUR_LINKED -> {
                run { //TODO удалить после отладки
                    testCounter++
                    if (testCounter > 3) {
                        logicCallback.onWorkCompleted()
                    } else {
                        logicCallback.onCellChanged(0,0)
                    }
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
    fun onCellChanged(x: Int, y: Int)
    fun onWorkCompleted()
}