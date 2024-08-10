package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readName
import nz.laspruca.wasmbc.wasm.readToEnd

class CustomSection(val name: String, val bytes: ByteArray) : Section() {
    override fun printDetails() {
        println("#section custom")
        println("\t{ name: $name, length: ${bytes.size} }")
    }
}

fun parseCustomSection(wasm: WasmReader): CustomSection {
    val name = wasm.run(::readName)
    val bytes = wasm.runBytes(::readToEnd)
    return CustomSection(name, bytes)
}