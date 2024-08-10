package nz.laspruca.wasmbc.wasm

data class Value<T>(val nBytesRead: Int, val value: T) {
}