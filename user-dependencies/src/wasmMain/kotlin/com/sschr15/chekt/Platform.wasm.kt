package com.sschr15.chekt

@PublishedApi
internal var printedWarning: Boolean = false

@PublishedApi
internal actual inline fun warnIfNegativeRem(a: Int, b: Int) {
    if (a < 0 && a != b && !printedWarning) {
        println("Warning: Remainder of $a (a negative number)")
        printedWarning = true
    }
}

@PublishedApi
internal actual inline fun warnIfNegativeRem(a: Long, b: Long) {
    if (a < 0 && a != b && !printedWarning) {
        println("Warning: Remainder of $a (a negative number)")
    }
}
