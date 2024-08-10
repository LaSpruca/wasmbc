package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.datatypes.Code
import nz.laspruca.wasmbc.wasm.datatypes.parseCode
import nz.laspruca.wasmbc.wasm.readVector

class CodeSection(val codeBlocks: List<nz.laspruca.wasmbc.wasm.datatypes.Code>) : Section() {
    override fun printDetails() {
        println("#section code")
        for (code in codeBlocks) {
            println(code)
        }
        println()
    }
}

fun parseCodeSection(wasm: WasmReader): CodeSection {
    return CodeSection(wasm.run(readVector(::parseCode)))
}