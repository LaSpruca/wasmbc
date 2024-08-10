package nz.laspruca.wasmbc.wasm.insturctions

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readUnsigned

@Opcode(0x10)
class Call(val functionIdx: UInt) : Instruction() {
    override fun toString() = "call($functionIdx);"
    companion object {
        @Parser
        fun parse(wasm: WasmReader) = Call(wasm.runBytes(::readUnsigned))
    }
}