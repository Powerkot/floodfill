package com.project.algorithm

interface Interaction {

    fun init(data: List<Cell>)

    fun start(x: Int, y: Int)

    fun next()

    fun get(): List<Cell>
}