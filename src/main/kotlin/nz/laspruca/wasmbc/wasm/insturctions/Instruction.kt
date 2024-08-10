package nz.laspruca.wasmbc.wasm.insturctions

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readNextByte
import kotlin.reflect.full.*
import kotlin.reflect.typeOf

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Opcode(val opcode: Byte)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Parser

sealed class Instruction {}

val instructionMap = buildMap {
    for (sealedSub in Instruction::class.sealedSubclasses) {
        val opcode = sealedSub.findAnnotation<Opcode>()
        val parser = sealedSub.companionObject?.functions?.find { it.hasAnnotation<Parser>() }
        if (opcode == null || parser == null) {
            System.err.println("${sealedSub.qualifiedName} is not a valid opcode class")
            continue
        }

        val parameters = parser.parameters
        val returns = parser.returnType

        if (parameters.size != 2 || parameters[1].type != typeOf<WasmReader>() || !returns.isSubtypeOf(typeOf<Instruction>())) {
            System.err.println("${sealedSub.qualifiedName} is not a valid opcode class")
            continue
        }

        put(opcode.opcode) { wasm: WasmReader -> parser.call(sealedSub.companionObjectInstance, wasm) !! as Instruction }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun parseInstruction(wasm: WasmReader): Instruction {
    val opcode = wasm.runBytes(::readNextByte)
    val parser = instructionMap[opcode] ?: throw RuntimeException("Unknown opcode 0x${opcode.toHexString()}")
    return parser(wasm)
}
