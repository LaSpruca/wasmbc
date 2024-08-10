package nz.laspruca

import nz.laspruca.wasmbc.module.validateModule
import nz.laspruca.wasmbc.wasm.parseWasm
import java.io.File

fun main() {
    val bytes = File("target.wasm").readBytes()
    val rawModule = parseWasm(bytes)
    rawModule.printDetails()

    var module = validateModule(rawModule)
}