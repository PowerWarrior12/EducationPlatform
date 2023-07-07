package com.example.educationtools.utils.extensions

import com.github.adriankuta.datastructure.tree.TreeNode

fun <T: Any?> TreeNode<T>.consists(stepsCounter: Int, predicate: (T) -> Boolean): Boolean {
    if (stepsCounter <= 0) return false
    if (predicate(value)) return true
    for (child in children) {
        if (child.consists(stepsCounter - 1, predicate)) return true
    }
    return false
}

fun <T: Any?> TreeNode<T>.firstOrNull(stepsCounter: Int, predicate: (T) -> Boolean): TreeNode<T>? {
    if (stepsCounter <= 0) return null
    if (predicate(value)) return this
    for (child in children) {
        child.firstOrNull(stepsCounter - 1, predicate)?.let { treeNode ->
            return treeNode
        }
    }
    return null
}