package nz.laspruca.wasmbc.wasm.insturctions

import nz.laspruca.wasmbc.wasm.WasmReader

@Opcode(0x1A)
class Drop : Instruction() {
    override fun toString() = "drop"

    companion object {
        @Parser
        fun parse(wasmReader: WasmReader) = Drop()
    }
}
