package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader

data class TableType(val et: RefType, val limits: Limits) {
    override fun toString(): String = "TableType { et: $et, limits: $limits }"
}

fun parseTableType(wasm: WasmReader) = TableType(wasm.run(::parseRefType), wasm.run(::parseLimits))