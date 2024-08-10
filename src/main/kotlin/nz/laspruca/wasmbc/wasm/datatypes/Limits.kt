package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readNextByte
import nz.laspruca.wasmbc.wasm.readUnsigned

class Limits(val lowerLimit: UInt, val upperLimit: UInt?) {
    override fun toString() = if (upperLimit != null) {
        "Limit { min: $lowerLimit, max: $upperLimit) }"
    } else {
        "Limit { min: $lowerLimit }"
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun parseLimits(wasm: WasmReader): Limits = when(val byte = wasm.runBytes(::readNextByte).toInt())  {
    0x00 -> Limits(wasm.runBytes(::readUnsigned), null)
    0x01 -> Limits(wasm.runBytes(::readUnsigned), wasm.runBytes(::readUnsigned))
    else -> throw RuntimeException("Received invalid byte when reading limits 0x${byte.toHexString()}")
}