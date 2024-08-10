package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readName

data class Import(val moduleName: String, val name: String, val importSpec: ImportSpec) {
    override fun toString() = "Import $moduleName:$name ($importSpec)"
}

fun parseImport(wasm: WasmReader) =
    Import(wasm.run(::readName), wasm.run(::readName), wasm.run(::parseImportSpec))