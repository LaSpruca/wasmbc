package nz.laspruca.wasmbc.wasm

import nz.laspruca.wasmbc.wasm.datatypes.FunctionType
import nz.laspruca.wasmbc.wasm.datatypes.ValueType
import java.lang.reflect.Method
import java.lang.reflect.Parameter


sealed class BindException(val method: Method, message: String) :
    RuntimeException("Error binding to java method ${method.declaringClass.canonicalName}:${method.name}: $message") {

    class WrongParameterCount(method: Method, wasmParams: Int) :
        BindException(
            method,
            "Java method takes ${method.parameters.size} parameters, but wasm function takes $wasmParams arguments, if this is an overloaded method, have you considered not overload it?"
        )

    class WrongParameterType(method: Method, index: Int, wasmType: ValueType) : BindException(
        method,
        "Parameter $index in method takes ${method.parameters[index].type} (which has wasm type ${method.parameters[index].type.wasmType()}), but wasm function accepts $wasmType"
    )

    class VoidReturn(method: Method, wasmType: ValueType) : BindException(
        method,
        "Method returns void (which is no return values in wasm) but wasm function return $wasmType"
    )

    class NonVoidReturn(method: Method) : BindException(
        method,
        "Method returns ${method.returnType} (which has wasm type ${method.returnType.wasmType()}) but wasm function return nothing"
    )

    class WrongReturn(method: Method, wasmType: ValueType) : BindException(
        method,
        "Method returns ${method.returnType} (which has wasm type ${method.returnType.wasmType()}), but wasm function returns $wasmType"
    )
}

fun validateParams(method: Method, wasmType: FunctionType) {
    if (method.parameters.size != wasmType.from.size) {
        throw BindException.WrongParameterCount(method, wasmType.from.size)
    }

    for ((index, parameter) in method.parameters.iterator().withIndex()) {
        if (parameter.type.wasmType() != wasmType.from[index]) {
            throw BindException.WrongParameterType(method, index, wasmType.from[index])
        }
    }

    if (wasmType.to.isEmpty() && method.returnType != Void.TYPE) {
        throw BindException.NonVoidReturn(method)
    } else if (wasmType.to.size == 1) {
        if (method.returnType == Void.TYPE) {
            throw BindException.VoidReturn(method, wasmType.to[0])
        } else if (method.returnType.wasmType() != wasmType.to[0]) {
            throw BindException.WrongReturn(method, wasmType.to[0])
        }
    } else if (wasmType.to.size > 1) {
        TODO("Wasm multiple return! (in ${method.name})")
    }

}

sealed class WasmFunction {
    abstract fun getWasmType(): FunctionType
    abstract fun javaParameters(): List<Parameter>
}

class ImportFunction(private val wasmType: FunctionType, javaClass: String, methodName: String) : WasmFunction() {
    private val method: Method

    init {
        val baseClass = Class.forName(javaClass)
        val maybeMethod = baseClass.methods.find { it.name == methodName }

        if (maybeMethod == null) {
            throw RuntimeException("Cannot find the method named $javaClass $methodName")
        }

        method = maybeMethod

        validateParams(method, wasmType)
    }

    override fun getWasmType() = wasmType

    override fun javaParameters() = method.parameters.toList()
}


private fun <T> Class<T>.wasmType(): ValueType = when (this) {
    Int::class.java -> ValueType.I32
    Long::class.java -> ValueType.I64
    Float::class.java -> ValueType.F32
    Double::class.java -> ValueType.F64

    Boolean::class.java -> ValueType.I32
    else -> ValueType.I32
}

class ExportFunction<T>(private val wasmType: FunctionType, javaClass: Class<T>, methodName: String) : WasmFunction() {
    private val method: Method

    init {
        val maybeMethod = javaClass.methods.find { it.name == methodName }

        if (maybeMethod == null) {
            throw RuntimeException("Cannot find the method named $methodName in base class ${javaClass.canonicalName}")
        }

        method = maybeMethod

        validateParams(method, wasmType)
    }

    override fun getWasmType() = wasmType

    override fun javaParameters(): List<Parameter> = method.parameters.toList()
}