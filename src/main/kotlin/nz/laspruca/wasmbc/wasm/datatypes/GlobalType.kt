package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readNextByte

data class GlobalType(val valueType: ValueType, val mutable: Boolean) {
    override fun toString() = if (mutable) {
        "var $valueType"
    } else {
        "const $valueType"
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun parseGlobalType(wasm: WasmReader): GlobalType {
    val valueType = wasm.run(::parseValueType)
    val mutable = when (val byte = wasm.runBytes(::readNextByte).toInt()) {
        0x00 -> false
        0x01 -> true
        else -> throw RuntimeException("Could not read global type mutability got byte ${byte.toHexString()}")
    }
    return GlobalType(valueType, mutable)
}
