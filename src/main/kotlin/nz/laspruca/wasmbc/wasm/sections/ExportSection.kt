package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.datatypes.Export
import nz.laspruca.wasmbc.wasm.datatypes.parseExport
import nz.laspruca.wasmbc.wasm.readVector

data class ExportSection(val exports: List<Export>) : Section() {
    override fun printDetails() {
        println("#section exports")
        for (export in exports) {
            println("\t$export")
        }
        println()
    }
}

fun parseExportSection(wasm: WasmReader) = ExportSection(wasm.run(readVector(::parseExport)))