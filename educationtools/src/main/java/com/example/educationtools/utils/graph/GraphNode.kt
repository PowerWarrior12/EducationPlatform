package com.example.educationtools.utils.graph

import com.github.adriankuta.datastructure.tree.TreeNode
import com.github.adriankuta.datastructure.tree.tree

class GraphNode<T>(val value: T) {
    private var _parents = mutableListOf<GraphNode<T>>()

    val parents: List<GraphNode<T>>
        get() = _parents

    private val _children = mutableListOf<GraphNode<T>>()

    val children: List<GraphNode<T>>
        get() = _children

    fun addChild(child: GraphNode<T>) {
        child._parents.add(this)
        _children.add(child)
    }

    val node = tree("string") {

    }


    fun removeChild(child: GraphNode<T>): Boolean {
        println(child.value)
        val removed = _children.remove(child)
        child._parents.remove(this)
        return removed
    }
}