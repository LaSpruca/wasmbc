package nz.laspruca.wasmbc

import nz.laspruca.wasmbc.wasm.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ValueReaderKtTest {
    @Test
    fun testDecodingUnsignedInt() {
        val bytes624485 = byteArrayOf(0xE5.toByte(), 0x8E.toByte(), 0x26, 0x00, 0x00)
        val read = readUnsigned(bytes624485)
        assertEquals(624485u, read.value)
        assertEquals(3, read.nBytesRead)
    }

    @Test
    fun testReadingBytes() {
        val originalBuffer = byteArrayOf(0x01, 0x02, 0x03, 0x04)
        val wasm = WasmReader(originalBuffer)
        val first = wasm.runBytes(::readNextByte)
        val second = wasm.runBytes(readNextN(2))
        val third = wasm.runBytes(::readNextByte)

        assertEquals(0x01, first)
        assertEquals(2, second.size)
        assertEquals(0x02, second[0])
        assertEquals(0x03, second[1])
        assertEquals(0x04, third)

        assert(!wasm.isNotEmpty())
    }

    @Test
    fun parseSignedInt() {
        val encoded = byteArrayOf(
            0xC0.toByte(),
            0xBB.toByte(),
        )

        val (bytesRead, decoded) = readI32(encoded)

        assertEquals(3, bytesRead)
        assertEquals(-123456, decoded)

        val (bytesRead2, decoded2) = readI32(encoded)
    }

}
