package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readNextByte
import nz.laspruca.wasmbc.wasm.readUnsigned

sealed class ImportSpec {
    data class TypeIndex(val index: UInt) : ImportSpec() {
        override fun toString() = "ImportSpec::TypeIndex($index)"
    }

    data class TableType(val tableType: nz.laspruca.wasmbc.wasm.datatypes.TableType) : ImportSpec() {
        override fun toString() = "ImportSpec::TableType($tableType)"
    }

    data class MemoryType(val limits: Limits) : ImportSpec() {
        override fun toString() = "ImportSpec::MemoryType($limits)"
    }

    data class GlobalType(val globalType: nz.laspruca.wasmbc.wasm.datatypes.GlobalType) : ImportSpec() {
        override fun toString() = "ImportSpec::GlobalType($globalType)"
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun parseImportSpec(wasm: WasmReader) = when(val byte = wasm.runBytes(::readNextByte).toInt()) {
    0x00 -> ImportSpec.TypeIndex(wasm.runBytes(::readUnsigned))
    0x01 -> ImportSpec.TableType(wasm.run(::parseTableType))
    0x02 -> ImportSpec.MemoryType(wasm.run(::parseLimits))
    0x03 -> ImportSpec.GlobalType(wasm.run(::parseGlobalType))
    else -> throw Error("Invalid import spec spec: 0x${byte.toHexString()}")
}