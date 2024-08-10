package nz.laspruca.wasmbc.wasm.sections

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.datatypes.Limits
import nz.laspruca.wasmbc.wasm.datatypes.parseLimits
import nz.laspruca.wasmbc.wasm.readVector

class MemorySection(val memories: List<Limits>) : Section() {
    override fun printDetails() {
        println("#section memory")
        for (memory in memories) {
            println("\t$memory")
        }
    }
}


fun parseMemorySection(wasm: WasmReader) = MemorySection(wasm.run(readVector(::parseLimits)))

