package nz.laspruca.wasmbc.module

import nz.laspruca.wasmbc.wasm.datatypes.ImportSpec
import nz.laspruca.wasmbc.wasm.sections.ImportSection
import nz.laspruca.wasmbc.wasm.Module as WasmModule

class Module() {
}


fun validateModule(module: WasmModule): Module {

    // Validate imports
    for (import in module.sections.filterIsInstance<ImportSection>().flatMap { item -> item.imports }) {
        when (import.importSpec) {
            is ImportSpec.GlobalType -> TODO()
            is ImportSpec.MemoryType -> TODO()
            is ImportSpec.TableType -> TODO()
            is ImportSpec.TypeIndex -> {

            }
        }
    }

    return Module()
}