package com.example.model

/**
 * Lightweight, self-contained QR Code generator in pure Kotlin.
 * Generates bit matrix for standard QR Code (Versions 1-6) with Byte mode and ECC level M.
 */
object QrCodeGenerator {

    data class QrMatrix(val size: Int, val modules: Array<BooleanArray>) {
        operator fun get(row: Int, col: Int): Boolean = modules[row][col]
    }

    /**
     * Generates a QR Code matrix from a text string.
     */
    fun encode(text: String): QrMatrix {
        val bytes = text.toByteArray(Charsets.UTF_8)
        val version = getMinVersion(bytes.size)
        val size = 17 + 4 * version
        val modules = Array(size) { BooleanArray(size) }
        val isFunction = Array(size) { BooleanArray(size) }

        // 1. Finder patterns
        placeFinderPattern(0, 0, modules, isFunction)
        placeFinderPattern(size - 7, 0, modules, isFunction)
        placeFinderPattern(0, size - 7, modules, isFunction)

        // 2. Timing patterns
        for (i in 8 until size - 8) {
            val bit = (i % 2 == 0)
            modules[6][i] = bit
            isFunction[6][i] = true
            modules[i][6] = bit
            isFunction[i][6] = true
        }

        // 3. Dark module
        modules[size - 8][8] = true
        isFunction[size - 8][8] = true

        // 4. Alignment patterns for version >= 2
        val alignCoords = getAlignmentPatternCoords(version)
        for (r in alignCoords) {
            for (c in alignCoords) {
                if (isFunction[r][c]) continue
                placeAlignmentPattern(r, c, modules, isFunction)
            }
        }

        // 5. Reserve format info
        reserveFormatInfo(size, isFunction)

        // 6. Encode data + ECC
        val bitBuffer = createBitBuffer(bytes, version)
        val dataWithEcc = addErrorCorrection(bitBuffer, version)

        // 7. Place data bits using mask pattern 0 ( (row + col) % 2 == 0 )
        placeData(dataWithEcc, modules, isFunction, size)

        // 8. Place format info with mask 0 and ECC M (00 000)
        placeFormatInfo(modules, size)

        return QrMatrix(size, modules)
    }

    private fun getMinVersion(dataLen: Int): Int {
        // Capacities for ECC M:
        val capacities = intArrayOf(0, 14, 26, 42, 61, 84, 106, 122, 152)
        for (v in 1 until capacities.size) {
            if (dataLen <= capacities[v]) return v
        }
        return 6
    }

    private fun placeFinderPattern(row: Int, col: Int, modules: Array<BooleanArray>, isFunc: Array<BooleanArray>) {
        for (r in -1..7) {
            for (c in -1..7) {
                val cr = row + r
                val cc = col + c
                if (cr in modules.indices && cc in modules.indices) {
                    val inCenter = (r in 0..6 && c in 0..6)
                    val isBlack = inCenter && (r == 0 || r == 6 || c == 0 || c == 6 || (r in 2..4 && c in 2..4))
                    modules[cr][cc] = isBlack
                    isFunc[cr][cc] = true
                }
            }
        }
    }

    private fun placeAlignmentPattern(centerR: Int, centerC: Int, modules: Array<BooleanArray>, isFunc: Array<BooleanArray>) {
        for (r in -2..2) {
            for (c in -2..2) {
                val cr = centerR + r
                val cc = centerC + c
                val isBlack = (abs(r) == 2 || abs(c) == 2 || (r == 0 && c == 0))
                modules[cr][cc] = isBlack
                isFunc[cr][cc] = true
            }
        }
    }

    private fun abs(v: Int) = if (v < 0) -v else v

    private fun getAlignmentPatternCoords(version: Int): IntArray = when (version) {
        2 -> intArrayOf(6, 18)
        3 -> intArrayOf(6, 22)
        4 -> intArrayOf(6, 26)
        5 -> intArrayOf(6, 30)
        6 -> intArrayOf(6, 34)
        else -> intArrayOf()
    }

    private fun reserveFormatInfo(size: Int, isFunc: Array<BooleanArray>) {
        for (i in 0..8) {
            isFunc[8][i] = true
            isFunc[i][8] = true
            isFunc[8][size - 1 - i] = true
            isFunc[size - 1 - i][8] = true
        }
    }

    private fun createBitBuffer(data: ByteArray, version: Int): BooleanArray {
        val bits = mutableListOf<Boolean>()
        // Mode indicator: Byte mode is 0100
        addBits(bits, 4, 4)
        // Character count indicator: 8 bits for versions 1-9
        addBits(bits, data.size, 8)
        // Data bits
        for (b in data) {
            addBits(bits, b.toInt() and 0xFF, 8)
        }
        // Terminator
        val totalCapacityBits = getTotalDataBytes(version) * 8
        val terminatorLen = (totalCapacityBits - bits.size).coerceIn(0, 4)
        addBits(bits, 0, terminatorLen)
        // Pad to byte
        while (bits.size % 8 != 0) bits.add(false)
        // Pad bytes 0xEC, 0x11
        var padToggle = true
        while (bits.size < totalCapacityBits) {
            val pad = if (padToggle) 0xEC else 0x11
            addBits(bits, pad, 8)
            padToggle = !padToggle
        }
        return bits.toBooleanArray()
    }

    private fun getTotalDataBytes(version: Int): Int = when (version) {
        1 -> 16
        2 -> 28
        3 -> 44
        4 -> 64
        5 -> 86
        6 -> 108
        else -> 108
    }

    private fun getEccBytes(version: Int): Int = when (version) {
        1 -> 10
        2 -> 16
        3 -> 26
        4 -> 36
        5 -> 48
        6 -> 64
        else -> 64
    }

    private fun addBits(list: MutableList<Boolean>, value: Int, length: Int) {
        for (i in length - 1 downTo 0) {
            list.add(((value shr i) and 1) == 1)
        }
    }

    private fun addErrorCorrection(dataBits: BooleanArray, version: Int): ByteArray {
        val dataBytes = ByteArray(dataBits.size / 8) { i ->
            var byte = 0
            for (b in 0 until 8) {
                if (dataBits[i * 8 + b]) byte = byte or (1 shl (7 - b))
            }
            byte.toByte()
        }

        val numEcc = getEccBytes(version)
        val ecc = ReedSolomon.computeEcc(dataBytes, numEcc)
        val result = ByteArray(dataBytes.size + ecc.size)
        System.arraycopy(dataBytes, 0, result, 0, dataBytes.size)
        System.arraycopy(ecc, 0, result, dataBytes.size, ecc.size)
        return result
    }

    private fun placeData(data: ByteArray, modules: Array<BooleanArray>, isFunc: Array<BooleanArray>, size: Int) {
        var byteIdx = 0
        var bitIdx = 7
        var upward = true
        var c = size - 1

        while (c > 0) {
            if (c == 6) c-- // Skip vertical timing pattern
            val cols = intArrayOf(c, c - 1)
            val rows = if (upward) (size - 1 downTo 0).toList() else (0 until size).toList()

            for (r in rows) {
                for (col in cols) {
                    if (!isFunc[r][col]) {
                        var bit = false
                        if (byteIdx < data.size) {
                            bit = ((data[byteIdx].toInt() shr bitIdx) and 1) == 1
                            bitIdx--
                            if (bitIdx < 0) {
                                bitIdx = 7
                                byteIdx++
                            }
                        }
                        // Mask pattern 0: (row + col) % 2 == 0
                        val mask = (r + col) % 2 == 0
                        modules[r][col] = bit xor mask
                    }
                }
            }
            upward = !upward
            c -= 2
        }
    }

    private fun placeFormatInfo(modules: Array<BooleanArray>, size: Int) {
        // ECC M (00) + Mask 0 (000) = 0b00000 -> Format bits with BCH: 101010000010010 xor 101010000010010 = 0
        val formatBits = 0x5412 // Standard precomputed format bits for ECC M + Mask 0 xor mask
        for (i in 0 until 15) {
            val bit = ((formatBits shr (14 - i)) and 1) == 1
            // Top left around finder
            val (r1, c1) = when {
                i < 6 -> Pair(8, i)
                i == 6 -> Pair(8, 7)
                i == 7 -> Pair(8, 8)
                i == 8 -> Pair(7, 8)
                else -> Pair(14 - i, 8)
            }
            modules[r1][c1] = bit

            // Split copy
            val (r2, c2) = if (i < 8) {
                Pair(size - 1 - i, 8)
            } else {
                Pair(8, size - 15 + i)
            }
            modules[r2][c2] = bit
        }
    }
}

/**
 * Minimal Galois Field GF(256) Reed-Solomon generator for QR Code error correction.
 */
object ReedSolomon {
    private val exp = IntArray(512)
    private val log = IntArray(256)

    init {
        var x = 1
        for (i in 0 until 255) {
            exp[i] = x
            log[x] = i
            x = x shl 1
            if (x >= 256) x = x xor 0x11D
        }
        for (i in 255 until 512) {
            exp[i] = exp[i - 255]
        }
    }

    private fun mul(a: Int, b: Int): Int = if (a == 0 || b == 0) 0 else exp[log[a] + log[b]]

    fun computeEcc(data: ByteArray, numEcc: Int): ByteArray {
        var gen = intArrayOf(1)
        for (i in 0 until numEcc) {
            val nextGen = IntArray(gen.size + 1)
            val factor = exp[i]
            for (j in gen.indices) {
                nextGen[j] = nextGen[j] xor gen[j]
                nextGen[j + 1] = nextGen[j + 1] xor mul(gen[j], factor)
            }
            gen = nextGen
        }

        val msg = IntArray(data.size + numEcc)
        for (i in data.indices) {
            msg[i] = data[i].toInt() and 0xFF
        }

        for (i in data.indices) {
            val lead = msg[i]
            if (lead != 0) {
                for (j in gen.indices) {
                    msg[i + j] = msg[i + j] xor mul(gen[j], lead)
                }
            }
        }

        val ecc = ByteArray(numEcc)
        for (i in 0 until numEcc) {
            ecc[i] = msg[data.size + i].toByte()
        }
        return ecc
    }
}
