package com.example.educationtools.logic

import com.example.educationtools.utils.graph.GraphNode
import com.github.adriankuta.datastructure.tree.TreeNode

class MemoryModel {

    //Коллекция блоков для анализа видимости переменных
    private val blocksAreaViewMap = mutableMapOf<String, TreeNode<Block>>()

    //Коллекция блоков для анализа видимости переменных
    private val blocksAreaLinkMap = mutableMapOf<String, GraphNode<Block>>()

    //Коллекция всех переменных
    private val variables = mutableMapOf<String, Variable>()

    private val freeBlocks = mutableListOf<TreeNode<Block>>()

    fun declareVarBlock(blockId: String) {
        blocksAreaViewMap[blockId] = TreeNode(Block.VariableBlock(blockId, null))
        blocksAreaLinkMap[blockId] = GraphNode(Block.VariableBlock(blockId, null))
        freeBlocks.add(blocksAreaViewMap.getValue(blockId))
    }

    fun declareConditionBlock(blockId: String) {
        blocksAreaViewMap[blockId] = TreeNode(Block.ConditionBlock(blockId))
        blocksAreaLinkMap[blockId] = GraphNode(Block.VariableBlock(blockId, null))
        freeBlocks.add(blocksAreaViewMap.getValue(blockId))
    }

    fun declareWhileDoBlock(blockId: String) {
        blocksAreaViewMap[blockId] = TreeNode(Block.WhileDoBlock(blockId))
        blocksAreaLinkMap[blockId] = GraphNode(Block.VariableBlock(blockId, null))
        freeBlocks.add(blocksAreaViewMap.getValue(blockId))
    }

    fun declareDoWhileBlock(blockId: String) {
        blocksAreaViewMap[blockId] = TreeNode(Block.DoWhileBlock(blockId))
        blocksAreaLinkMap[blockId] = GraphNode(Block.VariableBlock(blockId, null))
        freeBlocks.add(blocksAreaViewMap.getValue(blockId))
    }

    fun declareVariable(blockId: String, variable: Variable) {
        val block = blocksAreaViewMap.getValue(blockId).value
        if (block is Block.VariableBlock) {
            variables[variable.name] = variable
            block.variable = variable
        }
    }

    fun bindBlockLinkOrThrow(parentId: String, childId: String) {
        val parentNode = blocksAreaLinkMap.getValue(parentId)
        val childNode = blocksAreaLinkMap.getValue(childId)

        val availableBlocks = if (parentNode.value is Block.DoWhileBlock) {
            getDoWhileNodes(parentNode)
        } else {
            null
        }
    }

    fun bindBlockViewOrThrow(parentId: String, childId: String) {
        val parentNode = blocksAreaViewMap.getValue(parentId)
        val childNode = blocksAreaViewMap.getValue(childId)

        //Если у блока родителя нет наследников и у наследника нет родителя то просто связываем эти блоки
        if ((parentNode.children.isEmpty() || parentNode.value is Block.ConditionBlock) && childNode.parent == null) {
            parentNode.addChild(childNode)
            return
        }

        if (parentNode.value is Block.DoWhileBlock) {
            if (parentNode.children.isNotEmpty()) {

            }
        }

        // Если соединяем блок с наследником, у которого уже есть родитель, значит это выход из if-else и связываем родителя условного блока с поступившим наследником
        if (childNode.parent != null) {
            childNode.firstAmongParentsOrNull { it is Block.ConditionBlock }?.parent?.let { newParent ->
                childNode.parent?.removeChild(childNode)
                newParent.addChild(childNode)
            }

        }


    }

    private fun getDoWhileNodes(parentNode: GraphNode<Block>): List<GraphNode<Block>> {
        if (parentNode.children.count() > 1) return emptyList()

        val cycleBlocks = mutableListOf<GraphNode<Block>>()
        val afterDoBlocks = mutableListOf<GraphNode<Block>>()

        var cycleNeed = true
        var afterDoNeed = true
        var nestedDoWhile = false
        val checkedWhileDo = mutableListOf<GraphNode<Block>>()
        val checkedIfElse = mutableListOf<GraphNode<Block>>()

        var parent: GraphNode<Block>? = parentNode

        //Ноды, доступные для создания связи типа цикл
        var checkConditionBlock = blocksAreaViewMap[parent!!.value.id]?.parent?.value
        if (checkConditionBlock is Block.ConditionBlock) {
            parent = blocksAreaLinkMap[checkConditionBlock.id]!!
            cycleBlocks.add(parent)
        } else {
            parent = parent.parents.firstOrNull()
        }

        var lastParent = parentNode
        while (parent != null) {
            //Смотрим, ссылался ли в данный блок ещё один Do-While цикл
            var anotherDoWhile =
                parent.parents.takeWhile { it.value.id != parent!!.value.id && it.value is Block.DoWhileBlock }

            //Если такой цикл существует, то это значит либо из проверяемого блока уже существует цикличная ссылка,
            //либо она исходит из внешнего цикла, либо исходит из внутреннего цикла

            //Уже имеется циклическая связь -> указываем, что связь для цикла не нужна
            if (anotherDoWhile.isNotEmpty()) {
                if (anotherDoWhile.contains(parentNode)) {
                    cycleBlocks.add(parent)
                    cycleNeed = false
                    break
                } else {
                    var parentNodeView = blocksAreaViewMap.getValue(parentNode.value.id)
                    val anotherDoWhileIds = anotherDoWhile.map { it.value.id }
                    parentNodeView.forEach { node ->
                        //Находим внешний Do-While, следовательно дальше искать доступные блоки нет смысла, а также, что с блоками
                        //вне внешнего цикла связи заключать нельзя
                        if (node.value.id in anotherDoWhileIds) {
                            nestedDoWhile = true
                            return@forEach
                        }
                    }
                    if (nestedDoWhile) {
                        break
                    }
                }
            }

            //Проверяем вложенный блок If-Else
            checkConditionBlock = blocksAreaViewMap[parent.value.id]?.parent?.value
            if (checkConditionBlock is Block.ConditionBlock) {
                parent = blocksAreaLinkMap[checkConditionBlock.id]!!
                checkedIfElse.add(parent)
                cycleBlocks.add(parent)
                continue
            }
            // Если встреченный блок - If-Else, то прерываем поиск
            if (parent.value is Block.ConditionBlock && !checkedIfElse.contains(parent)) {
                break
            }
            //Проверяем блок Do-While
            if (parent.value is Block.DoWhileBlock) {
                //Это означает, что цикл Do-While ещё не имеет обозначенную область do, на которую мы не можем ссылаться
                //На сам блок c условием ссылаться не можем, поэтому пропускаем его
                if (parent.children.count() == 1) {
                    lastParent = parent
                    parent = parent.children.first()
                    continue
                }
                //Переходим к началу блока do встреченного Do-While цикла
                var startDoBlock = parent.children.first { it.value.id != lastParent.value.id }
                cycleBlocks.add(startDoBlock)

                lastParent = startDoBlock.children.first { !anotherDoWhile.contains(it) }
                parent = startDoBlock
                continue
            }

            //Проверяем блок While-Do
            val parentWhileDoBlock = parent.parents.firstOrNull { it.value is Block.WhileDoBlock }
            if (parentWhileDoBlock != null && !checkedWhileDo.contains(parentWhileDoBlock)) {

                var cycleParent: TreeNode<Block>? = null
                parentWhileDoBlock.parents.forEach { node ->
                    var whileParent = blocksAreaViewMap.getValue(node.value.id)
                    if (whileParent.children.isEmpty()) {
                        cycleParent = whileParent
                    }
                }
                if (cycleParent != null) {
                    var inCycle = cycleParent!!.checkInParentsWhileTrue( { node ->
                        node.value.id == parentNode.value.id
                    }, { node ->
                        node.children.count() <= 1
                    } )

                    if (inCycle) {
                        afterDoNeed = false
                        break
                    }
                }
                lastParent = parent
                parent = parentWhileDoBlock
                checkedWhileDo.add(parentWhileDoBlock)
                continue
            }

            lastParent = parent
            cycleBlocks.add(parent)
            parent = parent.parents.firstOrNull()
        }

        // Когда проанализировали все блоки, доступные для цикла, выясним, для какой связи блоки можно искать
        // У кадого Do-While блока может быть только два наследника

        if (cycleNeed && parentNode.children.isNotEmpty()) {
            return cycleBlocks
        }
        afterDoBlocks.addAll(freeBlocks.map { blocksAreaLinkMap.getValue(it.value.id) })
        if (afterDoNeed && parentNode.children.isNotEmpty()) {
            return afterDoBlocks
        }

        val result = mutableListOf<GraphNode<Block>>()
        result.addAll(cycleBlocks)
        result.addAll(afterDoBlocks)
        return result
    }

    private fun getWhileDoNodes()

    private fun <T> TreeNode<Block>.parentsMap(action: (Block) -> T): List<T> {
        val result = mutableListOf<T>()
        val parent = this.parent
        while (parent != null) {
            result.add(action(parent.value))
        }
        return result
    }

    private fun <T> TreeNode<Block>.parentsMapWhileFalse(action: (Block) -> T, condition: (Block) -> Boolean): List<T> {
        val result = mutableListOf<T>()
        var parent = this.parent
        while (parent != null && condition(parent.value)) {
            result.add(action(parent.value))
            parent = parent.parent
        }
        return result
    }

    private fun <T> TreeNode<Block>.parentsMapWhileIncLastFalse(
        action: (Block) -> T, condition: (Block) -> Boolean
    ): List<T> {
        val result = mutableListOf<T>()
        var parent = this.parent
        while (parent != null) {
            result.add(action(parent.value))
            if (parent.value is Block.ConditionBlock) break
            parent = parent.parent
        }
        return result
    }

    private fun TreeNode<Block>.checkInParentsWhileTrue(
        checkCondition: (TreeNode<Block>) -> Boolean,
        conditionSearch: (TreeNode<Block>) -> Boolean
    ): Boolean {
        var parent = this.parent
        while (parent != null && conditionSearch(parent)) {
            if (checkCondition(parent))
            parent = parent.parent
        }
        return false
    }

    private fun TreeNode<Block>.firstAmongParentsOrNull(condition: (Block) -> Boolean): TreeNode<Block>? {
        val parent = this.parent
        while (parent != null) {
            if (condition(parent.value)) {
                return parent
            }
        }
        return null
    }

    sealed class Block(
        val id: String
    ) {
        class VariableBlock(id: String, var variable: Variable?) : Block(id)
        class ConditionBlock(id: String) : Block(id)
        class WhileDoBlock(id: String) : Block(id)
        class DoWhileBlock(id: String) : Block(id)
    }
}