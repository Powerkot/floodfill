package com.project.algorithm.algorithms

internal interface IAlgorithm {
    fun iteration()
    fun pushUniqueCell(widthIndex: Int, heightIndex: Int)
    fun isCollectionAlreadyContainThisCell(widthIndex: Int, heightIndex: Int): Boolean
}