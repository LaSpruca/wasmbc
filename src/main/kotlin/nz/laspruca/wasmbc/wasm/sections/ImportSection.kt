package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.datatypes.Import
import nz.laspruca.wasmbc.wasm.datatypes.parseImport
import nz.laspruca.wasmbc.wasm.readVector

data class ImportSection(val imports: List<Import>) : Section() {

    override fun printDetails() {
        println("#section imports")
        for (import in imports) {
            println("\t $import")
        }
        println()
    }
}

fun parseImportSection(wasm: WasmReader) = ImportSection(wasm.run(readVector(::parseImport)))
