package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.Value
import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.datatypes.Global
import nz.laspruca.wasmbc.wasm.datatypes.parseGlobal
import nz.laspruca.wasmbc.wasm.readVector

class GlobalSection(var globals: List<Global>) : Section() {
    override fun printDetails() {
        println("#section globals")
        for (global in globals) {
            println("\t$global")
        }
        println()
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun parseGlobalSection(wasm: WasmReader): GlobalSection {
    wasm.runBytes {
        for (byte in it) {
            print("${byte.toHexString()} ")
        }
        println()
        Value(0, null)
    }
    return GlobalSection(wasm.run(readVector(::parseGlobal)))}

