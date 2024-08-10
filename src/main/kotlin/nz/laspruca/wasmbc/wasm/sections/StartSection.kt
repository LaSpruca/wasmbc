package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readUnsigned

class StartSection(val index: UInt) : Section() {
    override fun printDetails() {
        println("#section start Func($index)\n")
    }
}

fun parseStartSection(wasm: WasmReader) =
    StartSection(wasm.runBytes(::readUnsigned))