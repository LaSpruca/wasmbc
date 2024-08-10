package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.datatypes.TableType
import nz.laspruca.wasmbc.wasm.datatypes.parseTableType
import nz.laspruca.wasmbc.wasm.readVector

data class TableSection(val tables: List<TableType>) : Section() {
    override fun printDetails() {
        println("#section table")
        for (table in tables) {
            println("\t$table")
        }
        println()
    }
}

fun parseTableSection(wasm: WasmReader) = TableSection(wasm.run(readVector(::parseTableType)))