package nz.laspruca.wasmbc.module

import nz.laspruca.wasmbc.wasm.datatypes.FunctionType
import java.lang.reflect.Method

data class MethodImport(public val boundMethod: Method) {
    fun validateWasm(type: FunctionType) {
    }
}
