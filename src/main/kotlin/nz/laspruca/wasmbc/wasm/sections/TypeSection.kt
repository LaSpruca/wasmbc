package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.datatypes.FunctionType
import nz.laspruca.wasmbc.wasm.datatypes.parseFunctionType
import nz.laspruca.wasmbc.wasm.readVector

data class TypeSection(val functionTypes: List<FunctionType>) : Section() {
    override fun printDetails() {
        println("#section type")
        for (functionType in functionTypes) {
            println("\t$functionType")
        }
        println()
    }
}


fun parseTypeSection(wasm: WasmReader): TypeSection =
    TypeSection(wasm.run(readVector(::parseFunctionType)))
