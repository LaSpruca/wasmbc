package nz.laspruca.wasmbc

import nz.laspruca.wasmbc.wasm.*

class ModuleClassBuilder<T>(module: Module, private val targetInterface: Class<T>) {
    private val functions = mutableListOf<WasmFunction>()

    init {
        if (!targetInterface.isInterface) {
            throw RuntimeException("${targetInterface.name} is not an interface")
        }

        val nonExported = mutableListOf<IndexedValue<FunctionListEntry>>()
        for ((index, function) in module.getFunctions().iterator().withIndex()) {
            when (function.source) {
                is FunctionSource.Code -> {
                    val exportedName = module.getExportedName(index)

                    if (exportedName == null) {
                        nonExported.add(IndexedValue(index, function))
                        continue
                    }

                    functions.add(
                        ExportFunction(
                            module.getType(function.typeId),
                            targetInterface,
                            exportedName
                        )
                    );
                }

                is FunctionSource.Import -> {
                    functions.add(
                        ImportFunction(
                            module.getType(function.typeId),
                            function.source.className,
                            function.source.methodName
                        )
                    )
                }
            }
        }
    }
}