package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader

class ElementSection : Section() {
    override fun printDetails() {
        println("#section elements")
        println()
    }
}

fun parseElementSection(wasm: WasmReader): ElementSection {
    return ElementSection()
}