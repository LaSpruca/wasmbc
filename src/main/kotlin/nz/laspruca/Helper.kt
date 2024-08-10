package nz.laspruca

import nz.laspruca.wasmbc.WasmStr

object Helper {
    @JvmStatic
    fun say(@WasmStr str: String?) {
        println(str)
    }
}