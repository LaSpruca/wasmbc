package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader

class DataSection : Section() {
    override fun printDetails() {
        println("#section data")
        println()
    }
}


fun parseDataSection(wasm: WasmReader): DataSection {
    return DataSection()
}