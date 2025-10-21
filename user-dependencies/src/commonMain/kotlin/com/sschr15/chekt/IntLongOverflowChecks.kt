@file:Suppress("NOTHING_TO_INLINE")

package com.sschr15.chekt

import kotlin.math.absoluteValue

public inline fun plus(a: Int, b: Int): Int {
    val res = a + b
    if ((a xor res) and (b xor res) < 0) {
        throw ArithmeticException("Integer overflow")
    }
    return res
}

public inline fun minus(a: Int, b: Int): Int {
    val res = a - b
    if ((a xor b) and (a xor res) < 0) {
        throw ArithmeticException("Integer overflow")
    }
    return res
}

public inline fun times(a: Int, b: Int): Int {
    val res = a.toLong() * b.toLong()
    if (res != res.toInt().toLong()) {
        throw ArithmeticException("Integer overflow")
    }
    return res.toInt()
}

public inline fun inc(a: Int): Int {
    if (a == Int.MAX_VALUE) {
        throw ArithmeticException("Integer overflow")
    }
    return a + 1
}

public inline fun dec(a: Int): Int {
    if (a == Int.MIN_VALUE) {
        throw ArithmeticException("Integer overflow")
    }
    return a - 1
}

public inline fun unaryMinus(a: Int): Int {
    if (a == Int.MIN_VALUE) {
        throw ArithmeticException("Integer overflow")
    }
    return -a
}

public inline fun abs(a: Int): Int {
    if (a == Int.MIN_VALUE) {
        throw ArithmeticException("Integer overflow")
    }
    return kotlin.math.abs(a)
}

public inline fun rem(a: Int, b: Int): Int {
    warnIfNegativeRem(a, b)
    return a % b
}

public inline fun toFloat(a: Int): Float =
    if (a > 16777216 || a < -16777216) throw ArithmeticException("Not enough precision") else a.toFloat()

public inline fun plus(a: Long, b: Long): Long {
    val res = a + b
    if ((a xor res) and (b xor res) < 0) {
        throw ArithmeticException("Integer overflow")
    }
    return res
}

public inline fun minus(a: Long, b: Long): Long {
    val res = a - b
    if ((a xor b) and (a xor res) < 0) {
        throw ArithmeticException("Integer overflow")
    }
    return res
}

public inline fun times(a: Long, b: Long): Long {
    // Special handling: there isn't a larger data type to encapsulate the operation
    if (((a.absoluteValue or b.absoluteValue) ushr 31) != 0L) {
        val res = a * b
        if ((b != 0L && res / b != a) || (a == Long.MIN_VALUE && b == -1L)) {
            throw ArithmeticException("Integer overflow")
        }
        return res
    }
    // No chance for overflow
    return a * b
}

public inline fun inc(a: Long): Long {
    if (a == Long.MAX_VALUE) {
        throw ArithmeticException("Integer overflow")
    }
    return a + 1
}

public inline fun dec(a: Long): Long {
    if (a == Long.MIN_VALUE) {
        throw ArithmeticException("Integer overflow")
    }
    return a - 1
}

public inline fun unaryMinus(a: Long): Long {
    if (a == Long.MIN_VALUE) {
        throw ArithmeticException("Integer overflow")
    }
    return -a
}

public inline fun abs(a: Long): Long {
    if (a == Long.MIN_VALUE) {
        throw ArithmeticException("Integer overflow")
    }
    return kotlin.math.abs(a)
}

public inline fun rem(a: Long, b: Long): Long {
    warnIfNegativeRem(a, b)
    return a % b
}

public inline fun toInt(a: Long): Int = if (a > Int.MAX_VALUE || a < Int.MIN_VALUE) throw ArithmeticException("Not enough precision") else a.toInt()
public inline fun toFloat(a: Long): Float =
    if (a > 16777216 || a < -16777216) throw ArithmeticException("Not enough precision") else a.toFloat()
public inline fun toDouble(a: Long): Double =
    if (a > 9007199254740992 || a < -9007199254740992) throw ArithmeticException("Not enough precision") else a.toDouble()
