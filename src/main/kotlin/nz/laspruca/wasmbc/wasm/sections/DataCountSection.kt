package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader

class DataCountSection : Section() {
    override fun printDetails() {
        println("#section data_count")
    }
}

fun parseDataCountSection(wasm: WasmReader): DataCountSection {
    return DataCountSection()
}