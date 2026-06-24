package com.timas.superapp

import kotlinx.browser.window

actual fun showToast(message: String) {
    window.alert(message)
}

actual fun openUrl(url: String) {
    window.open(url, "_blank")
}
