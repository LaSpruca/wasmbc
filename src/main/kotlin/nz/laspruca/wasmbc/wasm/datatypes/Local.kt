package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readUnsigned

data class Local(val n: UInt, val valueType: ValueType) {
    override fun toString() = "Local$$n($valueType)"
}

fun parseLocal(wasm: WasmReader) = Local(wasm.runBytes(::readUnsigned), wasm.run(::parseValueType))