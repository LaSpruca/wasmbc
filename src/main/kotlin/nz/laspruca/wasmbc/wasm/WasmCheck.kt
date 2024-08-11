package nz.laspruca.wasmbc.wasm

import nz.laspruca.wasmbc.Module

private val MAGIC_NUMBER = byteArrayOf(0x00, 0x61, 0x73, 0x6D)
private val VERSION = byteArrayOf(0x01, 0x00, 0x00, 0x00)

private fun checkModule(wasm: WasmReader) {
    // Check the binary sequence starts with the magic number and the correct version
    if (!(MAGIC_NUMBER contentEquals wasm.runBytes(readNextN(4)))) {
        throw RuntimeException("Could not compile due to bad magic number")
    }


    if (!(VERSION contentEquals wasm.runBytes(readNextN(4)))) {
        throw RuntimeException("Could not compile due to bad version")
    }
}

fun parseWasm(bytes: ByteArray): Module {
    val wasm = WasmReader(bytes)

    checkModule(wasm)
    return Module(wasm)
}
