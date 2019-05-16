package com.project.algorithm

import java.util.*

interface Interaction {

    /**
     * Генерация матрицы изображения
     * @param imgSizeX количество строк
     * @param imgSizeY количество столбцов
     */
    fun generate(imgSizeX: Int, imgSizeY: Int)

    fun init(image: Array<BitSet>)

    /**
     * Установка начальных координат и алгоритма, выполнение первого шага
     * @param x начальная координата по оси X
     * @param y начальная координата по оси Y
     * @param algorithmType выбранный тип алгоритма
     */
    fun start(x: Int, y: Int, algorithmType: AlgorithmType, logicCallback: LogicCallback)

    /**
     * Выполняет один проход по матрице установленным алгоритмом
     * @return матрица после прохода
     */
    fun next(): Array<BitSet>

    fun getImage(): Array<BitSet>
}