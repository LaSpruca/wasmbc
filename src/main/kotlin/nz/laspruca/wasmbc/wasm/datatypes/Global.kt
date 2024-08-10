package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.insturctions.Expression
import nz.laspruca.wasmbc.wasm.insturctions.parseExpression

data class Global(val globalType: GlobalType, val init: Expression) {
    override fun toString() = "$globalType = { $init }"
}

fun parseGlobal(wasm: WasmReader) = Global(wasm.run(::parseGlobalType), wasm.run(::parseExpression))
