package com.sschr15.chekt

@PublishedApi
internal actual inline fun warnIfNegativeRem(a: Int, b: Int) {
    if (a < 0 && b != a && System.getProperty("aoc.warnOnNegativeRemainder") != "false") {
        System.err.println("Warning: Remainder of $a (a negative number)")
        System.setProperty("aoc.warnOnNegativeRemainder", "false")
    }
}

@PublishedApi
internal actual inline fun warnIfNegativeRem(a: Long, b: Long) {
    if (a < 0 && b != a && System.getProperty("aoc.warnOnNegativeRemainder") != "false") {
        System.err.println("Warning: Remainder of $a (a negative number)")
        System.setProperty("aoc.warnOnNegativeRemainder", "false")
    }
}
