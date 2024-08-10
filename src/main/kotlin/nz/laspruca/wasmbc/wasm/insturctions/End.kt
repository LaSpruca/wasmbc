package nz.laspruca.wasmbc.wasm.insturctions

import nz.laspruca.wasmbc.wasm.WasmReader

@Opcode(0x0B)
class End : Instruction() {
    companion object {
        @Parser
        fun parse(ignored: WasmReader) = End()
    }
}