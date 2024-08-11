package nz.laspruca

import nz.laspruca.wasmbc.ModuleClassBuilder
import nz.laspruca.wasmbc.wasm.parseWasm
import java.io.File

fun main() {
    val bytes = File("target.wasm").readBytes()
    val module = parseWasm(bytes)

    module.printDetails()

    val cb = ModuleClassBuilder(module, Application::class.java)
}
