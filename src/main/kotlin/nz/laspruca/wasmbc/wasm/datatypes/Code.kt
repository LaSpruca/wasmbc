package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.insturctions.Expression
import nz.laspruca.wasmbc.wasm.insturctions.parseExpression
import nz.laspruca.wasmbc.wasm.readNextN
import nz.laspruca.wasmbc.wasm.readUnsigned
import nz.laspruca.wasmbc.wasm.readVector

data class Code(val locals: List<nz.laspruca.wasmbc.wasm.datatypes.Local>, val code: Expression) {
    override fun toString() = "\tLocals { ${locals.joinToString(", ")} }\n\t { ${code.toString().replace(";", ";\n\t  ")} }"
}

fun parseCode(wasm: WasmReader): nz.laspruca.wasmbc.wasm.datatypes.Code {
    val size = wasm.runBytes(::readUnsigned)
    val newReader = WasmReader(wasm.runBytes(readNextN(size.toInt())))

    return nz.laspruca.wasmbc.wasm.datatypes.Code(
        newReader.run(readVector(::parseLocal)),
        newReader.run(::parseExpression)
    )
}
