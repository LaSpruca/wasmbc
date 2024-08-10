package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readNextByte

enum class ValueType {
    I32, I64, F32, F64, V128, FuncRef, ExternRef,
}

@OptIn(ExperimentalStdlibApi::class)
fun parseValueType(wasm: WasmReader): ValueType = when (val byte = wasm.runBytes(::readNextByte).toInt()) {
    0x7F -> ValueType.I32
    0x7E -> ValueType.I64
    0x7D -> ValueType.F32
    0x7C -> ValueType.F64
    0x7B -> ValueType.V128
    0x70 -> ValueType.FuncRef
    0x6F -> ValueType.ExternRef
    else -> throw RuntimeException("Invalid Value Type byte, got 0x${byte.toHexString()}")
}