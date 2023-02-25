package com.example.educationtools.logic

class TypeFunction(private val _type: String, private val value: Any): Function() {
    override var parameters: List<Parameter>
        get() = emptyList()
        set(value) {}

    override var type: String
        get() = _type
        set(value) {}

    override fun run(): Any {
        return value
    }

    companion object {
        inline fun<reified T: Any> generateTypeFunction(value: T): TypeFunction {
            return TypeFunction(T::class.toString(), value)
        }
    }
}