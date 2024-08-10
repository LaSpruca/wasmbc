package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readUnsigned
import nz.laspruca.wasmbc.wasm.readVector

class FunctionSection(val functions: List<UInt>) : Section() {
    override fun printDetails() {
        println("#section functions")
        for (function in functions) {
            println("\t $function")
        }
        println()
    }
}

fun parseFunctionSection(wasm: WasmReader) =
    FunctionSection(wasm.run(readVector { wasm -> wasm.runBytes(::readUnsigned) }))
