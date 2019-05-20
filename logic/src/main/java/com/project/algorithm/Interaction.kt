package com.project.algorithm

import com.project.algorithm.algorithms.AlgorithmType
import java.util.*

interface Interaction {

    /**
     * Инициализация по готовому изображению
     * @param imgWidth ширина (количество столбцов)
     * @param imgHeight высота (количество строк)
     * @param image инстанс матрицы изображения
     */
    fun setImg(imgWidth: Int, imgHeight: Int, image: Array<BitSet>)

    /**
     * Установка начальных координат и алгоритма, выполнение первого шага
     * @param startWidth начальная координата по оси X
     * @param startHeight начальная координата по оси Y
     * @param algorithmType тип первого алгоритма
     * @param logicCallback коллбэк результатов первого алгоритма
     */
    fun start(startWidth: Int, startHeight: Int, algorithmType: AlgorithmType, logicCallback: LogicCallback)

    /**
     * Выполняет один проход по матрице установленным алгоритмом
     */
    fun next()

    fun getImage(): Array<BitSet>
}

interface LogicCallback {
    fun onSingleIterationCompleted()
    fun onAllIterationsCompleted()
}