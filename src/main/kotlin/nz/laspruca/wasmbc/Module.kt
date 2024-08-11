package nz.laspruca.wasmbc

import nz.laspruca.wasmbc.wasm.WasmReader
import nz.laspruca.wasmbc.wasm.datatypes.*
import nz.laspruca.wasmbc.wasm.readNextByte
import nz.laspruca.wasmbc.wasm.readNextN
import nz.laspruca.wasmbc.wasm.readUnsigned
import nz.laspruca.wasmbc.wasm.sections.*

sealed class FunctionSource {
    data class Code(val functionCodeIndex: Int) : FunctionSource()
    data class Import(val className: String, val methodName: String) : FunctionSource()
}

data class FunctionListEntry(val typeId: UInt, val source: FunctionSource) {}

class Module(wasm: WasmReader) {
    private val customSections: HashMap<String, ByteArray> = hashMapOf()
    private val types = mutableListOf<FunctionType>()
    private val functions = mutableListOf<FunctionListEntry>()
    private val exports = mutableListOf<Export>()
    private val codeBlocks = mutableListOf<Code>()

    init {
        var currentFunctionCodeIndex = 0
        while (wasm.isNotEmpty()) {
            val sectionId = wasm.runBytes(::readNextByte).toInt()

            val sectionLength = wasm.runBytes(::readUnsigned)
            val sectionReader = WasmReader(wasm.runBytes(readNextN(sectionLength.toInt())))

            when (sectionId) {
                0 -> {
                    val section = parseCustomSection(sectionReader);
                    customSections[section.name] = section.bytes
                }

                1 -> {
                    types.addAll(parseTypeSection(sectionReader).functionTypes)
                }

                2 -> {
                    for (import in parseImportSection(sectionReader).imports) {
                        when (import.importSpec) {
                            is ImportSpec.GlobalType -> TODO()
                            is ImportSpec.MemoryType -> TODO()
                            is ImportSpec.TableType -> TODO()
                            is ImportSpec.TypeIndex -> {
                                functions.add(
                                    FunctionListEntry(
                                        import.importSpec.index, FunctionSource.Import(import.moduleName, import.name)
                                    )
                                )
                            }
                        }
                    }
                }

                3 -> {
                    for (function in parseFunctionSection(sectionReader).functions) {
                        functions.add(FunctionListEntry(function, FunctionSource.Code(currentFunctionCodeIndex)))
                        currentFunctionCodeIndex++
                    }
                }

                4 -> parseTableSection(sectionReader)
                5 -> parseMemorySection(sectionReader)
                6 -> parseGlobalSection(sectionReader)
                7 -> exports.addAll(parseExportSection(sectionReader).exports)
                8 -> parseTableSection(sectionReader)
                9 -> parseElementSection(sectionReader)
                10 -> codeBlocks.addAll(parseCodeSection(sectionReader).codeBlocks)
                11 -> parseDataSection(sectionReader)
                12 -> parseDataCountSection(sectionReader)
                else -> {
                    printDetails()
                    throw RuntimeException("Invalid section ID $sectionId")
                }
            }

        }

    }

    fun getType(index: Int) = types[index]
    fun getType(index: UInt) = types[index.toInt()]
    fun getFunctions() = functions

    fun printDetails() {
        for (customSection in customSections) {
            println("@customSection ${customSection.key} [0..${customSection.value.size}];")
        }

        println()

        for ((index, function) in functions.iterator().withIndex()) {
            when (function.source) {
                is FunctionSource.Code -> {
                    print("\nfn $$index")
                    val exportedName = getExportedName(index);
                    if (exportedName != null) {
                        print(" $exportedName")
                    }

                    val code = getCodeBlock(function.source.functionCodeIndex)
                    println("${getType(function.typeId)} {")
                        for (instr in code.code.instructions) {
                            println("\t$instr;")
                        }
                    println("}")
                }

                is FunctionSource.Import -> {
                    println("@import fn $$index ${function.source.className}:${function.source.methodName}${getType(function.typeId)};")
                }
            }
        }
    }

    private fun getExportedName(functionIndex: UInt) = exports.find {
        (it.exportDesc is ExportDesc.FunctionIndex) && (it.exportDesc.index == functionIndex)
    }?.name

    fun getExportedName(functionIndex: Int) = getExportedName(functionIndex.toUInt())
    fun getCodeBlock(index: Int) = codeBlocks[index]
}
