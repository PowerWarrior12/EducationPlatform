package com.example.educationtools.utils.extensions

fun String.split(indexes: List<Int>): List<String> {
    val result = mutableListOf<String>()
    var lastIndex = 0
    for (ind in indexes) {
        if (ind != 0) {
            result.add(this.substring(lastIndex, ind))
        }
        lastIndex = ind+1
    }
    result.add(this.substring(lastIndex, this.length))
    return result
}