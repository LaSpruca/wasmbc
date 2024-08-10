package nz.laspruca.wasmbc.wasm

import nz.laspruca.wasmbc.wasm.sections.*

class Module {
    var sections: ArrayList<Section> = arrayListOf()

    fun printDetails() {
        println("Module")

        for (section in sections) {
            section.printDetails()
        }
    }


    fun addSection(section: Section) {
        sections.add(section)
    }
}

fun parseModule(wasm: WasmReader): Module {
    val module = Module()

    while (wasm.isNotEmpty()) {
        val sectionId = wasm.runBytes(::readNextByte).toInt()

        val sectionLength = wasm.runBytes(::readUnsigned)
        val sectionReader = WasmReader(wasm.runBytes(readNextN(sectionLength.toInt())))

        val section = when (sectionId) {
            0 -> parseCustomSection(sectionReader)
            1 -> parseTypeSection(sectionReader)
            2 -> parseImportSection(sectionReader)
            3 -> parseFunctionSection(sectionReader)
            4 -> parseTableSection(sectionReader)
            5 -> parseMemorySection(sectionReader)
            6 -> parseGlobalSection(sectionReader)
            7 -> parseExportSection(sectionReader)
            8 -> parseTableSection(sectionReader)
            9 -> parseElementSection(sectionReader)
            10 -> parseCodeSection(sectionReader)
            11 -> parseDataSection(sectionReader)
            12 -> parseDataCountSection(sectionReader)
            else -> {
                module.printDetails()
                throw RuntimeException("Invalid section ID $sectionId")
            }
        }

        module.addSection(section)
    }

    return module
}
