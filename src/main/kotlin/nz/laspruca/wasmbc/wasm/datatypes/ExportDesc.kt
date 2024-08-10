package nz.laspruca.wasmbc.wasm.datatypes

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.readNextByte
import nz.laspruca.wasmbc.wasm.readUnsigned

sealed class ExportDesc {
    data class FunctionIndex(val index: UInt) : ExportDesc() {
        override fun toString() = "ExportDesc::FunctionIndex($index)"

    }

    data class TableIndex(val index: UInt) : ExportDesc() {
        override fun toString() = "ExportDesc::TableIndex($index)"
    }

    data class MemoryIndex(val mem: UInt) : ExportDesc() {
        override fun toString() = "ExportDesc::MemoryIndex($mem)"
    }

    data class GlobalIndex(val mem: UInt) : ExportDesc() {
        override fun toString() = "ExportDesc::GlobalIndex($mem)"
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun parseExportDesc(wasm: WasmReader): ExportDesc {
    val byte = wasm.runBytes(::readNextByte).toInt();
    val index = wasm.runBytes(::readUnsigned)

    return when (byte) {
        0x00 -> ExportDesc.FunctionIndex(index)
        0x02 -> ExportDesc.TableIndex(index)
        0x03 -> ExportDesc.MemoryIndex(index)
        0x04 -> ExportDesc.GlobalIndex(index)
        else -> throw RuntimeException("Could not determine type of input invalid byte 0x${byte.toHexString()}")
    }
}