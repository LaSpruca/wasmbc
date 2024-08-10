package nz.laspruca.wasmbc.wasm

class WasmReader(private var wasm: ByteArray) {
    fun<T> runBytes(function: (wasm: ByteArray) -> Value<T>): T {
        val (nRead, value) = function(wasm)
        wasm = wasm.sliceArray(nRead..wasm.lastIndex)
        return value
    }

    fun<T> run(function: (wasm: WasmReader) -> T): T = function(this)

    fun isNotEmpty(): Boolean = wasm.isNotEmpty()
}
