package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readNextByte

enum class RefType {
    FuncRef {
        override fun toString() = "RefType(FuncRef)"

        fun getValueType() = ValueType.FuncRef
    },
    ExternRef {
        override fun toString() = "RefType(ExternRef)"

        fun getValueType() = ValueType.ExternRef
    },
}

@OptIn(ExperimentalStdlibApi::class)
fun parseRefType(wasm: WasmReader) = when(val byte =wasm.runBytes(::readNextByte).toInt()) {
    0x70 -> RefType.FuncRef
    0x6F -> RefType.ExternRef
    else ->  throw RuntimeException("Invalid Ref Type ${byte.toHexString()}")
}
