@file:OptIn(ExperimentalForeignApi::class)
@file:Suppress("NOTHING_TO_INLINE")

package com.sschr15.chekt

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.fprintf
import platform.posix.getenv
import platform.posix.stderr

@PublishedApi
internal const val ENV_NAME: String = "KT_REM_WARNINGS"

@PublishedApi
internal expect inline fun disableWarning()

@PublishedApi
internal actual inline fun warnIfNegativeRem(a: Int, b: Int) {
    if (a < 0 && b != a && getenv(ENV_NAME)?.toKString() != "false") {
        fprintf(stderr, "Warning: Remainder of $a (a negative number)")
        disableWarning()
    }
}

@PublishedApi
internal actual inline fun warnIfNegativeRem(a: Long, b: Long) {
    if (a < 0 && b != a && getenv(ENV_NAME)?.toKString() != "false") {
        fprintf(stderr, "Warning: Remainder of $a (a negative number)")
        disableWarning()
    }
}
