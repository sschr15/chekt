package com.sschr15.chekt

import platform.posix.setenv

@PublishedApi
internal actual inline fun disableWarning() {
    setenv(ENV_NAME, "false", 1)
}
