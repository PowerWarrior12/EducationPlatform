package com.example.educationtools.logic

import android.util.Log
import com.github.adriankuta.datastructure.tree.TreeNode

/**
 * Класс для управления видимостью объявленных переменных и связями между блоками
 */
class MemoryModel {
    //Коллекция блоков для анализа видимости переменных
    private val blocksMap = mutableMapOf<String, TreeNode<Block>>()

    //Коллекция всех переменных
    private val variables = mutableMapOf<String, Variable>()

    //Блоки, свободные для связывания
    private val freeBlocks = mutableListOf<TreeNode<Block>>()

    /**
     * Объявление блока с переменной
     * @param blockId id объявляемого блока
     */
    fun declareVarBlock(blockId: String) {
        blocksMap[blockId] = TreeNode(Block.VariableBlock(blockId, null))
        freeBlocks.add(blocksMap.getValue(blockId))
    }

    /**
     * Объявление блока if-else
     * @param blockId id объявляемого блока
     */
    fun declareConditionBlock(blockId: String) {
        val conditionBlock: TreeNode<Block> = TreeNode(Block.ConditionBlock(blockId))
        conditionBlock.addChild(TreeNode(Block.FalseBlock()))
        conditionBlock.addChild(TreeNode(Block.TrueBlock()))
        blocksMap[blockId] = conditionBlock
        freeBlocks.add(blocksMap.getValue(blockId))
    }

    /**
     * Объявление блока цикла while-do
     * @param blockId id объявляемого блока
     */
    fun declareWhileDoBlock(blockId: String) {
        val whileDoBlock: TreeNode<Block> = TreeNode(Block.WhileDoBlock(blockId))
        whileDoBlock.addChild(TreeNode(Block.FalseBlock()))
        whileDoBlock.addChild(TreeNode(Block.TrueBlock()))
        blocksMap[blockId] = whileDoBlock
        freeBlocks.add(blocksMap.getValue(blockId))
    }

    /**
     * Объявление блока цикла do-while
     * @param blockId id объявляемого блока
     */
    fun declareDoWhileBlock(blockId: String) {
        blocksMap[blockId] = TreeNode(Block.DoWhileBlock(blockId))
        freeBlocks.add(blocksMap.getValue(blockId))
    }

    /**
     * Получение переменной по имени из общего словаря
     * @param variableName имя переменной
     */
    fun getVariable(variableName: String): Variable {
        return variables.getValue(variableName)
    }

    /**
     * Получение имени переменной по id блока, если в блоке не содержится переменной, то возвращается null
     * @param blockId id блока
     */
    fun getBlockVariable(blockId: String): String? {
        val block = blocksMap.getValue(blockId)
        return if (block.value is Block.VariableBlock) {
            (block.value as Block.VariableBlock).variableName
        } else {
            null
        }
    }

    /**
     * Объявление переменной. Нужно объявлять для объявленного блока
     * @param blockId блок, в котором объявляется переменная, блок должен быть объявлен
     * @param variable переменная, которую необходимо объявить
     */
    fun declareVariable(blockId: String, variable: Variable) {
        val block = blocksMap.getValue(blockId).value
        if (block is Block.VariableBlock) {
            variables[variable.name] = variable
            block.variableName = variable.name
        }
    }

    /**
     * Обновление переменной. Обновить можно только заранее объявленную переменную
     * @param variable переменная для обновления. Должно содержать имя, которое уже объявлено
     */
    fun updateVariable(variable: Variable) {
        variables.getValue(variable.name).value = variable.value
    }

    /**
     * Удаление переменной, переменная и блок, в котором это переменная находится должны быть объявлены
     * @param blockId блок, в котором удаляется переменная, блок должен быть объявлен
     * @param variable переменная, которую необходимо удалить, должна быть объявлена
     */
    fun deleteVariable(blockId: String, variable: Variable) {
        val block = blocksMap.getValue(blockId).value
        if (block is Block.VariableBlock) {
            variables.remove(variable.name)
            block.variableName = null
        }
    }

    /**
     * Получение списка id доступных для связи из указанного блока блоков. Исходный блок должен быть объявлен, иначе вызывается ошибка
     * @param id идентификатор блока, для которого ищутся доступные для связи блоки. Должен быть объявлен
     * @param direction направление связи, указывается для соответствующих блоков: ConditionBlock, WhileDoBlock,
     * DoWhileBlock. По умолчанию равен null. Если не direction не указан для описанных блоков то вызываеися ошибка
     */
    fun getAvailableBlocksOrThrow(id: String, direction: Boolean? = null): List<String> {
        if (!blocksMap.contains(id)) {
            throw java.lang.Exception("Данного блока не существует")
        }
        var block = blocksMap.getValue(id)
        val result = mutableListOf<TreeNode<Block>>()
        //В случае с Variable block добавляем свободные блоки, while do block в который можно вернуться завершая цикл
        // и выход из конструкции if else
        if (block.value is Block.VariableBlock) {
            result.addAll(getFreeBlocks(block))
            endWhileDoBlocks(block)?.let {
                result.add(it)
            }
            if (block.children.isEmpty()) {
                result.addAll(getEndIfElseBlocks(block))
            }
        }
        //В случае с Condition Block необходимо искать доступные блоки от ветвей true или false (указывается в direction).
        //Добавляем свободные блоки и выход из конструкции if-else (выход может быть как из текущего if-else, так и из внешнего)
        if (block.value is Block.ConditionBlock) {
            if (direction == null) throw java.lang.Exception("Для условного блока необходимо указывать направление")
            block = if (direction) {
                block.children.first { it.value is Block.TrueBlock }
            } else {
                block.children.first { it.value is Block.FalseBlock }
            }
            result.addAll(getFreeBlocks(block))
            if (block.children.isEmpty()) {
                result.addAll(getEndIfElseBlocks(block))
            }
        }
        //В случае с While-Do Block если рассматриваем true наравление (начало цикла), то добавляем только свободные блоки.
        //Направление false (выход из цикла) включает в себя свободные блоки, окончание while-do цикла (внешнего) и выход из конструкции if-else
        if (block.value is Block.WhileDoBlock) {
            if (direction == null) throw java.lang.Exception("Для условного блока необходимо указывать направление")
            result.addAll(getFreeBlocks(block))
            if (!direction) {
                endWhileDoBlocks(block)?.let {
                    result.add(it)
                }
                if (block.children.first { it.value is Block.FalseBlock }.children.isEmpty()) {
                    result.addAll(getEndIfElseBlocks(block))
                }
            }
        }
        //В случае с Do-While Block в направление true (переход в цикл) ищем специфичные блоки, предшевствующие
        // данному блоку для возможности зацикливания. Направление false (выход из цикла) включает свободные блоки, окончание
        //цикла while-do, выход из конструкции if-else
        if (block.value is Block.DoWhileBlock) {
            if (direction == null) throw java.lang.Exception("Для блока While-Do необходимо указывать направление")
            if (direction) {
                result.addAll(doWhileEnd(block))
            } else {
                val falseBlock = getFalseBlockOfDoWhile(block)
                result.addAll(getFreeBlocks(block))
                endWhileDoBlocks(block)?.let {
                    result.add(it)
                }
                if (block.children.isEmpty() || falseBlock?.children?.isEmpty() != null) {
                    result.addAll(
                        if (falseBlock != null) {
                            getEndIfElseBlocks(falseBlock.parent!!) as MutableList<TreeNode<Block>>
                        } else {
                            getEndIfElseBlocks(block) as MutableList<TreeNode<Block>>
                        }
                    )
                }
            }
        }
        //Возвращаем список id найденных блоков
        return result.map { it.value.toString() }
    }

    fun bindBlocksOrThrow(parentId: String, childId: String, direction: Boolean? = null) {
        if (!blocksMap.contains(parentId) || !blocksMap.contains(childId)) {
            throw java.lang.Exception("Данного блока не существует")
        }
        var parentNode = blocksMap.getValue(parentId)
        var childNode = blocksMap.getValue(childId)
        Log.d("Block-Shames", "Связь блока $parentId с блоком $childId")
        freeBlocks.forEach {
            Log.d("Block-Shames", it.prettyString())
        }

        var variants = mutableListOf<TreeNode<Block>>()
        if (parentNode.value is Block.VariableBlock) {

            variants = getFreeBlocks(parentNode) as MutableList<TreeNode<Block>>
            if (childNode in variants) {
                freeBlocks.remove(childNode)
                if (childNode.parent != null && childNode.parent!!.parent != null) {
                    childNode = childNode.parent!!.parent!!
                }
                parentNode.addChild(childNode)
                return
            }
            if (childNode == endWhileDoBlocks(parentNode)) {
                parentNode.addChild(TreeNode(Block.EndWhileBlock()))
                return
            }
            if (parentNode.children.isEmpty()) {
                variants = getEndIfElseBlocks(parentNode) as MutableList<TreeNode<Block>>


                if (childNode in variants) {
                    val childIfElse = findUnusedIfElse(childNode)
                    val parentIfElse = findUnusedIfElse(parentNode)
                    val needBlock = childIfElse.firstOrNull { it in parentIfElse }
                    if (needBlock != null) {
                        if (childNode.parent != null && childNode.parent!!.parent != null && childNode.parent!!.parent!!.value is Block.DoBlock) {
                            childNode.parent!!.parent!!.parent!!.removeChild(childNode.parent!!.parent!!)
                            needBlock.addChild(childNode.parent!!.parent!!)
                        } else {
                            childNode.parent!!.removeChild(childNode)
                            needBlock.addChild(childNode)
                        }
                        return
                    }
                }
            }
            throw java.lang.Exception("Невозможно связать блоки $parentId и $childId")
        }
        if (parentNode.value is Block.ConditionBlock) {
            if (direction == null) throw java.lang.Exception("Для условного блока необходимо указывать направление")

            parentNode = if (direction) {
                parentNode.children.first { it.value is Block.TrueBlock }
            } else {
                parentNode.children.first { it.value is Block.FalseBlock }
            }
            variants = getFreeBlocks(parentNode) as MutableList<TreeNode<Block>>
            if (childNode in variants) {
                freeBlocks.remove(childNode)
                if (childNode.parent != null && childNode.parent!!.parent != null) {
                    childNode = childNode.parent!!.parent!!
                }
                parentNode.addChild(childNode)
                return
            }
            if (parentNode.children.isEmpty()) {
                variants = getEndIfElseBlocks(parentNode) as MutableList<TreeNode<Block>>


                if (childNode in variants) {
                    val childIfElse = findUnusedIfElse(childNode)
                    val parentIfElse = findUnusedIfElse(parentNode)
                    val needBlock = childIfElse.firstOrNull { it in parentIfElse }
                    if (needBlock != null) {
                        if (childNode.parent != null && childNode.parent!!.parent != null && childNode.parent!!.parent!!.value is Block.DoBlock) {
                            childNode.parent!!.parent!!.parent!!.removeChild(childNode.parent!!.parent!!)
                            needBlock.addChild(childNode.parent!!.parent!!)
                        } else {
                            childNode.parent!!.removeChild(childNode)
                            needBlock.addChild(childNode)
                        }
                        return
                    }
                }
            }
            throw java.lang.Exception("Невозможно связать блоки $parentId и $childId")

        }
        if (parentNode.value is Block.WhileDoBlock) {
            if (direction == null) throw java.lang.Exception("Для блока While-Do необходимо указывать направление")
            variants = getFreeBlocks(parentNode) as MutableList<TreeNode<Block>>
            if (childNode in variants) {
                freeBlocks.remove(childNode)
                if (childNode.parent != null && childNode.parent!!.parent != null) {
                    childNode = childNode.parent!!.parent!!
                }
                if (direction) {
                    parentNode.children.first { it.value is Block.TrueBlock }.addChild(childNode)
                } else {
                    parentNode.children.first { it.value is Block.FalseBlock }.addChild(childNode)
                }

                return
            }

            if (direction) {
                throw java.lang.Exception("Невозможно связать блоки $parentId и $childId")
            } else {
                if (childNode == endWhileDoBlocks(parentNode)) {
                    parentNode.children.first { it.value is Block.FalseBlock }
                        .addChild(TreeNode(Block.EndWhileBlock()))
                    return
                }
                if (parentNode.children.first { it.value is Block.FalseBlock }.children.isEmpty()) {
                    variants = getEndIfElseBlocks(parentNode) as MutableList<TreeNode<Block>>

                    if (childNode in variants) {
                        val childIfElse = findUnusedIfElse(childNode)
                        val parentIfElse = findUnusedIfElse(parentNode)
                        val needBlock = childIfElse.firstOrNull { it in parentIfElse }
                        if (needBlock != null) {
                            if (childNode.parent != null && childNode.parent!!.parent != null && childNode.parent!!.parent!!.value is Block.DoBlock) {
                                childNode.parent!!.parent!!.parent!!.removeChild(childNode.parent!!.parent!!)
                                needBlock.addChild(childNode.parent!!.parent!!)
                            } else {
                                childNode.parent!!.removeChild(childNode)
                                needBlock.addChild(childNode)
                            }
                            return
                        }
                    }
                }
                throw java.lang.Exception("Невозможно связать блоки $parentId и $childId")
            }

        }
        if (parentNode.value is Block.DoWhileBlock) {
            if (direction == null) throw java.lang.Exception("Для блока While-Do необходимо указывать направление")

            if (direction) {
                variants = doWhileEnd(parentNode) as MutableList<TreeNode<Block>>
                if (childNode in variants) {
                    var childCurrentParent = childNode.parent
                    val doBlock: TreeNode<Block> = TreeNode(Block.DoBlock())
                    val trueBlock: TreeNode<Block> = TreeNode(Block.TrueBlock())
                    val falseBlock: TreeNode<Block> = TreeNode(Block.FalseBlock())
                    doBlock.addChild(trueBlock)
                    doBlock.addChild(falseBlock)

                    if (childCurrentParent?.parent != null && childCurrentParent.parent!!.value is Block.DoBlock) {
                        childNode = childCurrentParent.parent!!
                        childCurrentParent = childCurrentParent.parent!!.parent
                    }

                    if (childCurrentParent != null) {

                        childCurrentParent.removeChild(childNode)
                        childCurrentParent.addChild(doBlock)
                    }
                    trueBlock.addChild(childNode)
                    val doWhileChild = parentNode.children.firstOrNull()
                    if (doWhileChild != null) {
                        parentNode.removeChild(doWhileChild)
                        falseBlock.addChild(doWhileChild)
                    }
                    parentNode.addChild(TreeNode(Block.EndDoBlock()))
                    return
                }
            } else {
                val falseBlock = getFalseBlockOfDoWhile(parentNode)

                variants = getFreeBlocks(parentNode) as MutableList<TreeNode<Block>>
                if (childNode in variants) {
                    freeBlocks.remove(childNode)
                    if (childNode.parent != null && childNode.parent!!.parent != null) {
                        childNode = childNode.parent!!.parent!!
                    }
                    if (falseBlock != null) {
                        falseBlock.addChild(childNode)
                    } else {
                        parentNode.addChild(childNode)
                    }

                    return
                }
                if (childNode == endWhileDoBlocks(parentNode)) {
                    if (falseBlock != null) {
                        falseBlock.addChild(TreeNode(Block.EndWhileBlock()))
                    } else {
                        parentNode.addChild(TreeNode(Block.EndWhileBlock()))
                    }

                    return
                }
                if (parentNode.children.isEmpty() || falseBlock?.children?.isEmpty() != null) {
                    variants = if (falseBlock != null) {
                        getEndIfElseBlocks(falseBlock.parent!!) as MutableList<TreeNode<Block>>
                    } else {
                        getEndIfElseBlocks(parentNode) as MutableList<TreeNode<Block>>
                    }

                    if (childNode in variants) {
                        val childIfElse = findUnusedIfElse(childNode)
                        val parentIfElse = if (falseBlock != null) {
                            findUnusedIfElse(falseBlock.parent!!)
                        } else {
                            findUnusedIfElse(parentNode)
                        }
                        val needBlock = childIfElse.firstOrNull { it in parentIfElse }
                        if (needBlock != null) {
                            if (childNode.parent != null && childNode.parent!!.parent != null && childNode.parent!!.parent!!.value is Block.DoBlock) {
                                childNode.parent!!.parent!!.parent!!.removeChild(childNode.parent!!.parent!!)
                                needBlock.addChild(childNode.parent!!.parent!!)
                            } else {
                                childNode.parent!!.removeChild(childNode)
                                needBlock.addChild(childNode)
                            }
                            return
                        }
                    }
                }

            }
            throw java.lang.Exception("Невозможно связать блоки $parentId и $childId")
        }
    }

    fun getAvailableVariablesOrThrow(blockId: String): List<String> {
        val result = mutableListOf<String>()
        if (!blocksMap.contains(blockId)) {
            throw java.lang.Exception("Данного блока не существует")
        }
        var parent = blocksMap.getValue(blockId).parent

        while (parent != null) {
            if (parent.value is Block.VariableBlock) {
                (parent.value as Block.VariableBlock).variableName?.let {
                    result.add(it)
                }
            }
            parent = parent.parent
        }

        return result
    }

    fun getDependentBlocks(blockId: String): List<String> {
        val result = mutableListOf<String>()
        if (!blocksMap.contains(blockId)) {
            throw java.lang.Exception("Данного блока не существует")
        }
        fun recursiveSearch(block: TreeNode<Block>) {
            if (block.value is Block.MainBlock) {
                result.add((block.value as Block.MainBlock).id)
            }
            block.children.forEach {
                recursiveSearch(it)
            }
        }
        blocksMap.getValue(blockId).children.forEach {
            recursiveSearch(it)
        }
        return result
    }

    private fun getFalseBlockOfDoWhile(parentNode: TreeNode<Block>): TreeNode<Block>? {
        return if (parentNode.children.firstOrNull { it.value is Block.EndDoBlock } != null) {
            var parent = parentNode.parent
            while (parent != null) {
                if (parent.value is Block.TrueBlock && parent.parent!!.value is Block.DoBlock) {
                    parent = parent.parent!!.children.firstOrNull { it.value is Block.FalseBlock }
                    break
                }
                parent = parent.parent
            }
            parent
        } else {
            null
        }
    }

    private fun findUnusedIfElse(parentBlock: TreeNode<Block>): List<TreeNode<Block>> {
        var parent: TreeNode<Block>? = parentBlock
        val result = mutableListOf<TreeNode<Block>>()
        while (parent != null) {
            if ((parent.value is Block.FalseBlock || parent.value is Block.TrueBlock) && parent.parent!!.value is Block.ConditionBlock) {
                if (parent.parent!!.children.count() > 2) break
                result.add(parent.parent!!)
                parent = parent.parent!!.parent
                continue
            }

            if (parent.value is Block.TrueBlock) break
            parent = parent.parent
        }
        return result
    }

    /**
     * Поиск блока While-Do, к которому можно перейти из конца цикла
     * @param parentBlock блок от которого ищется переход
     */
    private fun endWhileDoBlocks(parentBlock: TreeNode<Block>): TreeNode<Block>? {
        if (parentBlock.children.firstOrNull { it.value is Block.EndWhileBlock } != null) {
            return null
        }
        var parent = parentBlock.parent

        while (parent != null) {
            if (parent.value is Block.TrueBlock && parent.parent!!.value is Block.WhileDoBlock) {
                return parent.parent!!
            }
            parent = parent.parent
        }
        return null
    }

    /**
     * Поиск свободных блоков
     * @param parentBlock блок, от которого необходимо построить связь
     */
    private fun getFreeBlocks(parentBlock: TreeNode<Block>): List<TreeNode<Block>> {
        var parent: TreeNode<Block>? = parentBlock
        var lastParent = parent
        while (true) {
            if (parent!!.parent == null) break
            if (parent.parent!!.value is Block.DoBlock) {
                parent = parent.parent
                continue
            }
            lastParent = parent
            parent = parent.parent
        }
        if (parent?.value is Block.DoBlock) {
            parent = lastParent
        }
        return freeBlocks.filter { it != parent }
    }

    /**
     * Поиск блока для завершения области If-Else
     * @param parentBlock блок, от которого необходимо построить связь
     */
    private fun getEndIfElseBlocks(parentBlock: TreeNode<Block>): List<TreeNode<Block>> {
        val resultList = mutableListOf<TreeNode<Block>>()

        fun findBlocks(conditionNode: Pair<TreeNode<Block>, TreeNode<Block>>) {
            var child =
                conditionNode.first.children.first { it != conditionNode.second }.firstOrNull()
            while (child != null) {
                if (child.value is Block.ConditionBlock) {
                    if (child.children.count() > 2) {
                        resultList.add(child)
                        child =
                            child.children.firstOrNull { it.value !is Block.TrueBlock && it.value !is Block.FalseBlock }
                        continue
                    } else {
                        findBlocks(
                            Pair(
                                child,
                                child.children.first { it.value is Block.TrueBlock })
                        )
                        findBlocks(
                            Pair(
                                child,
                                child.children.first { it.value is Block.FalseBlock })
                        )
                        break
                    }
                }
                if (child.value is Block.WhileDoBlock || child.value is Block.DoBlock) {
                    if (child.value !is Block.DoBlock) {
                        resultList.add(child)
                    }
                    child = child.children.first { it.value is Block.FalseBlock }
                    child = child.children.firstOrNull()
                    continue
                }
                if (child.value !is Block.EndWhileBlock && child.value !is Block.TrueBlock && child.value !is Block.FalseBlock) {
                    resultList.add(child)
                }
                child = child.children.firstOrNull()
            }
        }

        var parent: TreeNode<Block>? = parentBlock
        while (parent != null) {
            if ((parent.value is Block.FalseBlock || parent.value is Block.TrueBlock) && parent.parent!!.value is Block.ConditionBlock) {
                if (parent.parent!!.children.count() > 2) break
                findBlocks(Pair(parent.parent!!, parent))
                parent = parent.parent!!.parent
                continue
            }

            if (parent.value is Block.TrueBlock) break
            parent = parent.parent
        }

        return resultList
    }

    /**
     * Поиск блока для обозначения области do в цикле Do-While
     * @param parentBlock блок, от которого необходимо построить связь
     */
    private fun doWhileEnd(parentBlock: TreeNode<Block>): List<TreeNode<Block>> {
        var parent = parentBlock.parent
        val resultList = mutableListOf<TreeNode<Block>>()
        while (parent != null) {
            if ((parent.value is Block.TrueBlock || parent.value is Block.FalseBlock) && parent.parent!!.value is Block.ConditionBlock) {
                if (parent.parent!!.children.count() > 2) break
                resultList.add(parent.parent!!)
                parent = parent.parent!!.parent
                continue
            }

            if (parent.value is Block.TrueBlock) break
            if (parent.value is Block.FalseBlock) {
                parent = parent.parent
                continue
            }

            resultList.add(parent)
            parent = parent.parent
        }

        return resultList
    }

    private sealed class Block {
        open class MainBlock(val id: String) : Block()
        class VariableBlock(id: String, var variableName: String?) : MainBlock(id) {
            override fun toString(): String {
                return id
            }
        }

        class ConditionBlock(id: String) : MainBlock(id) {
            override fun toString(): String {
                return id
            }
        }

        class WhileDoBlock(id: String) : MainBlock(id) {
            override fun toString(): String {
                return id
            }
        }

        class DoWhileBlock(id: String) : MainBlock(id) {
            override fun toString(): String {
                return id
            }
        }

        class TrueBlock : Block() {
            override fun toString(): String {
                return "TRUE"
            }
        }

        class FalseBlock : Block() {
            override fun toString(): String {
                return "FALSE"
            }
        }

        class DoBlock : Block() {
            override fun toString(): String {
                return "DO"
            }
        }

        class EndWhileBlock : Block() {
            override fun toString(): String {
                return "END WHILE"
            }
        }

        class EndDoBlock : Block() {
            override fun toString(): String {
                return "END DO"
            }
        }
    }
}