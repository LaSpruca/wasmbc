package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readNextByte
import nz.laspruca.wasmbc.wasm.readVector

data class FunctionType(val t1: List<ValueType>, val t2: List<ValueType>) {
    override fun toString(): String = "Fn(${t1.joinToString(", ")}) -> (${t2.joinToString(", ")})"
}

@OptIn(ExperimentalStdlibApi::class)
fun parseFunctionType(wasm: WasmReader): FunctionType {
    val nextByte = wasm.runBytes(::readNextByte)
    if (nextByte != 0x60.toByte()) {
        throw RuntimeException("Expected function type to start with 0x60 got 0x${nextByte.toHexString()}")
    }

    val t1 = wasm.run(readVector(::parseValueType))
    val t2 = wasm.run(readVector(::parseValueType))

    return FunctionType(t1, t2)
}