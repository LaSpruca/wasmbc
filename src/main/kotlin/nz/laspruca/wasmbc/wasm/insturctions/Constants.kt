package nz.laspruca.wasmbc.wasm.insturctions

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readI32
import nz.laspruca.wasmbc.wasm.readI64

@Opcode(0x41)
data class ConstI32(val value: Int) : Instruction() {
    override fun toString() = "push ${value}i32"
    companion object {
        @Parser
        fun parse(wasm: WasmReader) = ConstI32(wasm.runBytes(::readI32))
    }
}

@Opcode(0x42)
data class ConstI64(val value: Long) : Instruction() {
    override fun toString() = "push ${value}i64"
    companion object {
        @Parser
        fun parse(wasm: WasmReader) = ConstI64(wasm.runBytes(::readI64))
    }

}