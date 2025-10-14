package com.sschr15.chekt

import platform.windows.SetEnvironmentVariableW

@PublishedApi
internal actual inline fun disableWarning() {
    // thanks, windows, for making cross-platform so complicated
    SetEnvironmentVariableW(ENV_NAME, "false")
}
