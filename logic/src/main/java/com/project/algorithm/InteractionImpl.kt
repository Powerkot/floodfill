package com.project.algorithm

import com.project.algorithm.algorithms.AlgIterator
import com.project.algorithm.algorithms.AlgorithmType
import com.project.algorithm.algorithms.FourWayAlg
import com.project.algorithm.algorithms.LineAlg
import com.project.algorithm.models.Cell
import java.util.*


class InteractionImpl : Interaction {

    private var imgWidth = -1
    private var imgHeight = -1
    private lateinit var image: Array<BitSet>
    private lateinit var algIterator: AlgIterator

    override fun setImg(imgWidth: Int, imgHeight: Int, image: Array<BitSet>) {
        this.imgWidth = imgWidth
        this.imgHeight = imgHeight
        this.image = image
    }

    override fun start(
        startWidth: Int,
        startHeight: Int,
        algorithmType: AlgorithmType,
        logicCallback: LogicCallback
    ) {
        val startCell = Cell(
            startWidth,
            startHeight,
            image[startHeight][startWidth]
        )

        when (algorithmType) {
            AlgorithmType.FOUR_WAY -> {
                algIterator = FourWayAlg(
                    image,
                    imgWidth,
                    imgHeight,
                    startCell,
                    logicCallback
                )
            }
            AlgorithmType.LINE -> {
                algIterator = LineAlg(
                    image,
                    imgWidth,
                    imgHeight,
                    startCell,
                    logicCallback
                )
            }
            AlgorithmType.THREE -> {
                //TODO
            }
        }

        next() //выполнение первого шага
    }

    override fun next() {
        algIterator.iteration()
    }

    override fun getImage(): Array<BitSet> = image
}