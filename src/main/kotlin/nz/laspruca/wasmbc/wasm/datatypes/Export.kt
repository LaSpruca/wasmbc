package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readName

data class Export(val name: String, val exportDesc: ExportDesc) {
    override fun toString() = "Export { name: $name, exportDesc: $exportDesc }"
}

fun parseExport(wasm: WasmReader) = Export(wasm.run(::readName), wasm.run(::parseExportDesc))