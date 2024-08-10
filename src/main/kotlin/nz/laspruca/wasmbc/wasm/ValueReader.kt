package nz.laspruca.wasmbc.wasm

/**
 * Read the rest of the buffer
 */
fun readToEnd(wasm: ByteArray): Value<ByteArray> = Value(wasm.size, wasm)

/**
 * Read the next n bytes
 */
fun readNextN(bytes: Int): (wasm: ByteArray) -> Value<ByteArray> = { wasm -> Value(bytes, wasm.sliceArray(0..<bytes)) }

/**
 * Read the next byte
 */
fun readNextByte(wasm: ByteArray): Value<Byte> = Value(1, wasm[0])

/**
 * Decode a unsigned integer in the lEB128
 *
 * @param wasm The WASM byte module
 * @see <a href="https://en.wikipedia.org/wiki/LEB128#Decode_unsigned_integer">Wikipedia Article</a>
 */
fun readUnsigned(wasm: ByteArray): Value<UInt> {
    var result = 0u
    var shift = 0
    var index = 0

    while (true) {
        val byte = wasm[index]
        result = result or ((0b0111_1111u and byte.toUInt()) shl shift)

        if ((byte.toUInt() and 0b1000_0000u) == 0u) {
            break;
        }

        index += 1
        shift += 7
    }

    return Value(index + 1, result)
}

fun readI32(wasm: ByteArray): Value<Int> {
    val (bytes, value) = readSigned(32, wasm)
    return Value(bytes, value.toInt())
}

fun readI64(wasm: ByteArray): Value<Long> = readSigned(64, wasm)

fun readSigned(size: Int, wasm: ByteArray): Value<Long> {
    var result: Long = 0
    var shift = 0
    var index = 0
    var byte: Byte

    do {
        byte = wasm[index]
        result = result or (byte.toLong() and 0b0111_1111 shl shift)
        shift += 7
        index++
    } while (byte.toInt() and 0b1000_0000 != 0)

    if ((shift < size) && (byte.toInt() and 0b0100_0000 == 0b0100_0000)) {
        result = (result or (0.toLong().inv() shl shift))
    }

    return Value(index , result)
}


/**
 * Read a vector parsing each element using the parse function
 *
 * @param parser The function to parse the individual elements of an array
 */
fun <T> readVector(parser: (WasmReader) -> T): (wasm: WasmReader) -> List<T> = { wasm ->
    val length = wasm.runBytes(::readUnsigned)
    List(length.toInt()) { parser(wasm) }
}


/**
 * Read a name from the binary
 *
 * @param wasm The WASM bytes
 * @see <a href=""https://webassembly.github.io/spec/core/binary/values.html#names>The specification</a>
 */
fun readName(wasm: WasmReader): String {
    val length = wasm.runBytes(::readUnsigned)
    val arr = wasm.runBytes(readNextN(length.toInt()))
    return arr.decodeToString()
}
