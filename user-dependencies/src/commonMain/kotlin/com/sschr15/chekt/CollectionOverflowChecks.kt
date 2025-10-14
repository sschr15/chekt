@file:OptIn(ExperimentalTypeInference::class)
@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.sschr15.chekt

import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName

@JvmName("sumOfBytes")
public inline fun sum(items: Iterable<Byte>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfBytes")
public inline fun sum(items: Sequence<Byte>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfBytes")
public inline fun sum(items: Array<Byte>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

public inline fun sum(items: ByteArray): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfBytesToBytes")
public inline fun sumOf(items: ByteArray, transform: (Byte) -> Byte): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfBytesToShorts")
public inline fun sumOf(items: ByteArray, transform: (Byte) -> Short): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfBytesToInts")
public inline fun sumOf(items: ByteArray, transform: (Byte) -> Int): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfBytesToLongs")
public inline fun sumOf(items: ByteArray, transform: (Byte) -> Long): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = transform(item).toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfCharsToBytes")
public inline fun sumOf(items: CharArray, transform: (Char) -> Byte): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfShorts")
public inline fun sum(items: Iterable<Short>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfShorts")
public inline fun sum(items: Sequence<Short>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfShorts")
public inline fun sum(items: Array<Short>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

public inline fun sum(items: ShortArray): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfShortsToBytes")
public inline fun sumOf(items: ShortArray, transform: (Short) -> Byte): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfShortsToShorts")
public inline fun sumOf(items: ShortArray, transform: (Short) -> Short): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfShortsToInts")
public inline fun sumOf(items: ShortArray, transform: (Short) -> Int): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfShortsToLongs")
public inline fun sumOf(items: ShortArray, transform: (Short) -> Long): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = transform(item).toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfCharsToShorts")
public inline fun sumOf(items: CharArray, transform: (Char) -> Short): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfInts")
public inline fun sum(items: Iterable<Int>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfInts")
public inline fun sum(items: Sequence<Int>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfInts")
public inline fun sum(items: Array<Int>): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

public inline fun sum(items: IntArray): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = item.toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfIntsToBytes")
public inline fun sumOf(items: IntArray, transform: (Int) -> Byte): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfIntsToShorts")
public inline fun sumOf(items: IntArray, transform: (Int) -> Short): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfIntsToInts")
public inline fun sumOf(items: IntArray, transform: (Int) -> Int): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfIntsToLongs")
public inline fun sumOf(items: IntArray, transform: (Int) -> Long): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = transform(item).toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfCharsToInts")
public inline fun sumOf(items: CharArray, transform: (Char) -> Int): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfLongs")
public inline fun sum(items: Iterable<Long>): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = item.toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfLongs")
public inline fun sum(items: Sequence<Long>): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = item.toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@JvmName("sumOfLongs")
public inline fun sum(items: Array<Long>): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = item.toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

public inline fun sum(items: LongArray): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = item.toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfLongsToBytes")
public inline fun sumOf(items: LongArray, transform: (Long) -> Byte): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfLongsToShorts")
public inline fun sumOf(items: LongArray, transform: (Long) -> Short): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfLongsToInts")
public inline fun sumOf(items: LongArray, transform: (Long) -> Int): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfLongsToLongs")
public inline fun sumOf(items: LongArray, transform: (Long) -> Long): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = transform(item).toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumOfCharsToLongs")
public inline fun sumOf(items: CharArray, transform: (Char) -> Long): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = transform(item).toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToBytes")
public inline fun <T> sumOf(items: Iterable<T>, transform: (T) -> Byte): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToShorts")
public inline fun <T> sumOf(items: Iterable<T>, transform: (T) -> Short): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToInts")
public inline fun <T> sumOf(items: Iterable<T>, transform: (T) -> Int): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToLongs")
public inline fun <T> sumOf(items: Iterable<T>, transform: (T) -> Long): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = transform(item).toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToBytes")
public inline fun <T> sumOf(items: Sequence<T>, transform: (T) -> Byte): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToShorts")
public inline fun <T> sumOf(items: Sequence<T>, transform: (T) -> Short): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToInts")
public inline fun <T> sumOf(items: Sequence<T>, transform: (T) -> Int): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToLongs")
public inline fun <T> sumOf(items: Sequence<T>, transform: (T) -> Long): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = transform(item).toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToBytes")
public inline fun <T> sumOf(items: Array<T>, transform: (T) -> Byte): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToShorts")
public inline fun <T> sumOf(items: Array<T>, transform: (T) -> Short): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToInts")
public inline fun <T> sumOf(items: Array<T>, transform: (T) -> Int): Int {
    var sum = 0
    for (item in items) {
        val prev = sum
        val num = transform(item).toInt()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}

@OverloadResolutionByLambdaReturnType
@JvmName("sumToLongs")
public inline fun <T> sumOf(items: Array<T>, transform: (T) -> Long): Long {
    var sum = 0L
    for (item in items) {
        val prev = sum
        val num = transform(item).toLong()
        sum += num
        if ((sum xor prev) and (sum xor num) < 0) {
            throw ArithmeticException("Integer overflow")
        }
    }
    return sum
}
