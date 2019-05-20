package com.project.algorithm

import java.util.*

class Generator {

    /**
     * Генерация матрицы изображения
     * @param imgHeight высота (количество строк)
     * @param imgWidth ширина (количество столбцов)
     * @param seed зерно
     * @return изображение
     */
    fun generate(imgHeight: Int, imgWidth: Int, seed: Long): Array<BitSet> {
        val random = Random(seed)
        return Array(imgHeight) {
            BitSet().apply {
                for (j in 0 until imgWidth) {
//                    set(j, false) //TODO временно все false для удобной отладки
                    set(j, random.nextFloat() <= 0.5)
                }
            }
        }
    }
}