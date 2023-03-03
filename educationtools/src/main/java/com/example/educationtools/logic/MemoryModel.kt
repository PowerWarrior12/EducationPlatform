package com.example.educationtools.logic

import android.util.Log
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
        blocksAreaLinkMap[blockId] = GraphNode(Block.ConditionBlock(blockId))
        freeBlocks.add(blocksAreaViewMap.getValue(blockId))
    }

    fun declareWhileDoBlock(blockId: String) {
        blocksAreaViewMap[blockId] = TreeNode(Block.WhileDoBlock(blockId))
        blocksAreaLinkMap[blockId] = GraphNode(Block.WhileDoBlock(blockId))
        freeBlocks.add(blocksAreaViewMap.getValue(blockId))
    }

    fun declareDoWhileBlock(blockId: String) {
        blocksAreaViewMap[blockId] = TreeNode(Block.DoWhileBlock(blockId))
        blocksAreaLinkMap[blockId] = GraphNode(Block.DoWhileBlock(blockId))
        freeBlocks.add(blocksAreaViewMap.getValue(blockId))
    }

    fun declareVariable(blockId: String, variable: Variable) {
        val block = blocksAreaViewMap.getValue(blockId).value
        if (block is Block.VariableBlock) {
            variables[variable.name] = variable
            block.variable = variable
        }
    }

    fun bindBlocks(parentId: String, childId: String) {
        Log.d("Block-Shames", "Связываем блоки:  $parentId and $childId")
        bindBlockLinkOrThrow(parentId, childId)

        bindBlockViewOrThrow(parentId, childId)
    }

    fun bindBlockLinkOrThrow(parentId: String, childId: String) {
        val parentNode = blocksAreaLinkMap.getValue(parentId)
        val childNode = blocksAreaLinkMap.getValue(childId)

        val availableBlocks = getDoWhileNodes(parentNode)
        Log.d("Block-Shames", "${availableBlocks.map { 
          it.value.id  
        }}")
        if (childNode !in availableBlocks) throw Exception("Error")
        parentNode.addChild(childNode)
    }

    fun bindBlockViewOrThrow(parentId: String, childId: String) {
        val parentNode = blocksAreaViewMap.getValue(parentId)
        val childNode = blocksAreaViewMap.getValue(childId)

        // Если у родителя нет наследников, а у наследника нет родителей, то связываем без доп трансформаций
        if (parentNode.children.isEmpty() && childNode.parent == null && parentNode.value !is Block.DoWhileBlock) {
            parentNode.addChild(childNode)
            freeBlocks.remove(childNode)
            freeBlocks.forEach {
                Log.d("Block-Shames",it.prettyString())
            }
            return
        }
        // Если родитель это условный блок, а у наследника нет родителей, то связываем без доп трансформаций
        if ((parentNode.value is Block.ConditionBlock || parentNode.value is Block.WhileDoBlock) && childNode.parent == null) {
            parentNode.addChild(childNode)
            freeBlocks.remove(childNode)
            freeBlocks.forEach {
                Log.d("Block-Shames",it.prettyString())
            }
            return
        }

        if (parentNode.value is Block.DoWhileBlock) {
            val parentNodeLink = blocksAreaLinkMap.getValue(parentId)
            if (parentNodeLink.children.count() == 1) {
                val isCycle = parentNode.checkInParentsWhileTrue({ node ->
                    node.value.id == childId
                }, { true })
                if (!isCycle && childNode.parent == null) {
                    parentNode.addChild(childNode)
                    freeBlocks.remove(childNode)
                    freeBlocks.forEach {
                        Log.d("Block-Shames",it.prettyString())
                    }
                    return
                }
            } else {
                val currentChildren = parentNode.children.firstOrNull()
                if (currentChildren != null) {
                    parentNode.removeChild(currentChildren)
                    childNode.addChild(currentChildren)
                } else {
                    val isPreviousWhileDo = parentNode.checkInParentsWhileTrue({ node ->
                        node.value.id == childId
                    }, { true })
                    if (!(childNode.value is Block.WhileDoBlock && isPreviousWhileDo) && childNode.parent == null) {
                        val cycleChild =
                            blocksAreaViewMap.getValue(parentNodeLink.children.first { it.value.id != childNode.value.id }.value.id)

                        cycleChild.addChild(childNode)
                        freeBlocks.remove(childNode)
                        freeBlocks.forEach {
                            Log.d("Block-Shames",it.prettyString())
                        }
                        return
                    }
                }
            }
        }

        if (childNode.value is Block.WhileDoBlock) {
            if (parentNode.checkInParentsWhileTrue({ it.value.id == childId }, { true })) {
                val translateChild = childNode.children.firstOrNull()
                if (translateChild != null) {
                    childNode.removeChild(translateChild)
                    childNode.addChild(translateChild)
                    freeBlocks.forEach {
                        Log.d("Block-Shames",it.prettyString())
                    }
                    return
                }
            }
        }

        if (childNode.parent != null && parentNode.value !is Block.WhileDoBlock) {
            val parentIfElse = mutableListOf<Pair<TreeNode<Block>, TreeNode<Block>>>()
            val childIfElse = mutableListOf<Pair<TreeNode<Block>, TreeNode<Block>>>()

            fun fillIfElse(list: MutableList<Pair<TreeNode<Block>, TreeNode<Block>>>, startNode: TreeNode<Block>) {
                var parent = startNode.parent
                var lastNode = startNode
                while (parent != null) {
                    if (parent.value is Block.ConditionBlock && parent.children.count() <= 2) {
                        list.add(Pair(parent, lastNode))
                    }
                    lastNode = parent
                    parent = parent.parent
                }
            }

            fillIfElse(parentIfElse, parentNode)
            fillIfElse(childIfElse, childNode)

            var findingBlock: TreeNode<Block>? = null
            for (pIfElse in parentIfElse) {
                for (cIfElse in childIfElse) {
                    if (pIfElse.first == cIfElse.first && pIfElse.second != cIfElse.second) {
                        findingBlock = pIfElse.first
                        break
                    }
                }
                if (findingBlock != null) {
                    break
                }
            }

            if (findingBlock != null) {
                childNode.parent?.removeChild(childNode)
                findingBlock.addChild(childNode)
            }
        }

        freeBlocks.forEach {
            Log.d("Block-Shames",it.prettyString())
        }
    }

    private fun getDoWhileNodes(parentNode: GraphNode<Block>): List<GraphNode<Block>> {
        if (parentNode.value is Block.VariableBlock) {
            if (parentNode.children.isNotEmpty()) return emptyList()
        } else {
            if (parentNode.children.count() > 1) return emptyList()
        }

        val cycleBlocks = mutableListOf<GraphNode<Block>>()
        val afterDoBlocks = mutableListOf<GraphNode<Block>>()

        var cycleNeed = true
        var afterDoNeed = true
        var nestedDoWhile = false
        val checkedWhileDo = mutableListOf<GraphNode<Block>>()
        val checkedIfElse = mutableListOf<GraphNode<Block>>()

        var parent: GraphNode<Block>? = parentNode
        var lastParent = parent?.children?.firstOrNull() ?: parentNode

        if (parent?.value is Block.WhileDoBlock) {
            checkedWhileDo.add(parent)
        }

        /*//Ноды, доступные для создания связи типа цикл
        var checkConditionBlock = blocksAreaViewMap[parent!!.value.id]?.parent?.value
        if (checkConditionBlock is Block.ConditionBlock) {
            parent = blocksAreaLinkMap[checkConditionBlock.id]!!
            cycleBlocks.add(parent)
        } else {
            parent = parent.parents.firstOrNull()
        }*/

        while (parent != null) {
            //Смотрим, ссылался ли в данный блок ещё один Do-While цикл
            var anotherDoWhile =
                parent.parents.filter { it.value is Block.DoWhileBlock }

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
            val checkConditionBlockParent = blocksAreaViewMap[parent.value.id]?.parent
            if (checkConditionBlockParent?.value is Block.ConditionBlock && checkConditionBlockParent.children.count() >= 2) {
                parent = blocksAreaLinkMap[checkConditionBlockParent.value.id]!!
                checkedIfElse.add(parent)
                cycleBlocks.add(parent)
                continue
            }
            // Если встреченный блок - If-Else, то прерываем поиск
            if (parent.value is Block.ConditionBlock && !checkedIfElse.contains(parent)) {
                break
            }
            //Проверяем блок Do-While
            if (parent.value is Block.DoWhileBlock && parent.value.id != parentNode.value.id) {
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

                lastParent = startDoBlock.children.first()
                parent = startDoBlock
                continue
            }

            //Проверяем блок While-Do
            val childWhileDoBlock = parent.children.firstOrNull { it.value is Block.WhileDoBlock }
            if (childWhileDoBlock != null && !checkedWhileDo.contains(childWhileDoBlock)) {
                lastParent =
                    blocksAreaLinkMap.getValue(blocksAreaViewMap.getValue(childWhileDoBlock.value.id).children.first().value.id)
                parent = childWhileDoBlock
                checkedWhileDo.add(childWhileDoBlock)
            }

            val parentWhileDoBlock = parent.parents.firstOrNull { it.value is Block.WhileDoBlock }
            if (parentWhileDoBlock != null && !checkedWhileDo.contains(parentWhileDoBlock)) {

                var cycleParent: TreeNode<Block>? = null
                parentWhileDoBlock.parents.forEach { node ->
                    var whileParent = blocksAreaViewMap.getValue(node.value.id)
                    if (whileParent.children.firstOrNull { it.value.id == parentWhileDoBlock.value.id} == null) {
                        cycleParent = whileParent
                    }
                }
                if (cycleParent != null) {
                    var inCycle = cycleParent!!.checkInParentsWhileTrue({ node ->
                        node.value.id == parentNode.value.id
                    }, { node ->
                        node.children.count() <= 1
                    })

                    if (inCycle) {
                        afterDoNeed = false
                        break
                    }
                }
                cycleBlocks.add(parent)
                lastParent = parent
                afterDoBlocks.add(parentWhileDoBlock)
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

        if (parentNode.value is Block.DoWhileBlock) {
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
        } else {
            afterDoBlocks.addAll(freeBlocks.map { blocksAreaLinkMap.getValue(it.value.id) })
            return afterDoBlocks
        }
    }

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
            if (checkCondition(parent)) {
                return true
            }
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

        override fun toString(): String {
            return id
        }
    }
}