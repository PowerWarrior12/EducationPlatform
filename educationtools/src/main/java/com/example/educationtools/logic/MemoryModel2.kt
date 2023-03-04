package com.example.educationtools.logic

import com.example.educationtools.utils.graph.GraphNode
import com.github.adriankuta.datastructure.tree.TreeNode

class MemoryModel2 {
    //Коллекция блоков для анализа видимости переменных
    private val blocksMap = mutableMapOf<String, TreeNode<Block>>()

    //Коллекция всех переменных
    private val variables = mutableMapOf<String, Variable>()

    private val freeBlocks = mutableListOf<TreeNode<Block>>()

    fun declareVarBlock(blockId: String) {
        blocksMap[blockId] = TreeNode(Block.VariableBlock(blockId, null))
        freeBlocks.add(blocksMap.getValue(blockId))
    }

    fun declareConditionBlock(blockId: String) {
        val conditionBlock: TreeNode<Block> = TreeNode(Block.ConditionBlock(blockId))
        conditionBlock.addChild(TreeNode(Block.FalseBlock()))
        conditionBlock.addChild(TreeNode(Block.TrueBlock()))
        blocksMap[blockId] = conditionBlock
        freeBlocks.add(blocksMap.getValue(blockId))
    }

    fun declareWhileDoBlock(blockId: String) {
        val whileDoBlock: TreeNode<Block> = TreeNode(Block.WhileDoBlock(blockId))
        whileDoBlock.addChild(TreeNode(Block.FalseBlock()))
        whileDoBlock.addChild(TreeNode(Block.TrueBlock()))
        blocksMap[blockId] = whileDoBlock
        freeBlocks.add(blocksMap.getValue(blockId))
    }

    fun declareDoWhileBlock(blockId: String) {
        blocksMap[blockId] = TreeNode(Block.DoWhileBlock(blockId))
        freeBlocks.add(blocksMap.getValue(blockId))
    }

    fun declareVariable(blockId: String, variable: Variable) {
        val block = blocksMap.getValue(blockId).value
        if (block is Block.VariableBlock) {
            variables[variable.name] = variable
            block.variable = variable
        }
    }

    private fun endWhileDoBlocks(parentBlock: TreeNode<Block>) {
        var parent = parentBlock.parent
        while (parent != null) {

        }
    }

    sealed class Block {
        class VariableBlock(val id: String, var variable: Variable?) : Block() {
            override fun toString(): String {
                return id
            }
        }
        class ConditionBlock(val id: String) : Block() {
            override fun toString(): String {
                return id
            }
        }
        class WhileDoBlock(val id: String) : Block() {
            override fun toString(): String {
                return id
            }
        }
        class DoWhileBlock(val id: String) : Block() {
            override fun toString(): String {
                return id
            }
        }
        class TrueBlock: Block()
        class FalseBlock: Block()
        class DoBlock: Block()
        class EndWhileBlock: Block()
    }
}