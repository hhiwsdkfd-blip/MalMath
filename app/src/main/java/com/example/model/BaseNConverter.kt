package com.example.model

enum class BaseMode(val base: Int, val prefix: String, val label: String) {
    DEC(10, "Dec", "DEC"),
    HEX(16, "Hex", "HEX"),
    BIN(2, "Bin", "BIN"),
    OCT(8, "Oct", "OCT")
}

object BaseNConverter {
    fun format(value: Long, base: BaseMode): String {
        return when (base) {
            BaseMode.DEC -> value.toString()
            BaseMode.HEX -> java.lang.Long.toHexString(value).uppercase()
            BaseMode.BIN -> java.lang.Long.toBinaryString(value)
            BaseMode.OCT -> java.lang.Long.toOctalString(value)
        }
    }

    fun parse(input: String, base: BaseMode): Long {
        val clean = input.trim()
        if (clean.isEmpty()) return 0L
        return java.lang.Long.parseUnsignedLong(clean, base.base)
    }

    fun bitAnd(a: Long, b: Long) = a and b
    fun bitOr(a: Long, b: Long) = a or b
    fun bitXor(a: Long, b: Long) = a xor b
    fun bitNot(a: Long) = a.inv()
    fun shiftLeft(a: Long, bits: Int) = a shl bits
    fun shiftRight(a: Long, bits: Int) = a shr bits
}
