package nz.laspruca.wasmbc.wasm.insturctions

import nz.laspruca.wasmbc.wasm.WasmReader

data class Expression(val instructions: List<Instruction>) {
    override fun toString() = instructions.joinToString("; ")
}

fun parseExpression(wasm: WasmReader): Expression {
    val instructions = mutableListOf<Instruction>();

    while (true) {
        val instruction = parseInstruction(wasm)
        if (instruction is End) {
            break
        }

        instructions.add(instruction)
    }

    return Expression(instructions)
}

