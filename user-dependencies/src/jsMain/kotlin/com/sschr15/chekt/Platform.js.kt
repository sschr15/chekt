package com.sschr15.chekt

import kotlinx.browser.document

@PublishedApi
internal actual inline fun warnIfNegativeRem(a: Int, b: Int) {
    if (a < 0 && b != a && "aoc.warnOnNegativeRemainder" !in document.cookie) {
        console.warn("Warning: Remainder of $a (a negative number)")
        document.cookie = "aoc.warnOnNegativeRemainder=true"
    }
}

@PublishedApi
internal actual inline fun warnIfNegativeRem(a: Long, b: Long) {
    if (a < 0 && b != a && "aoc.warnOnNegativeRemainder" !in document.cookie) {
        console.warn("Warning: Remainder of $a (a negative number)")
        document.cookie = "aoc.warnOnNegativeRemainder=true"
    }
}
