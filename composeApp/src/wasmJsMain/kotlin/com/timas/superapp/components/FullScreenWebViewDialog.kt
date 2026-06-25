package com.timas.superapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLIFrameElement
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLSpanElement

@Composable
actual fun FullScreenWebViewDialog(
    title: String,
    url: String,
    onDismiss: () -> Unit
) {
    DisposableEffect(url) {
        // 1. Create main container div
        val container = document.createElement("div") as HTMLDivElement
        container.style.setProperty("position", "fixed")
        container.style.setProperty("top", "0")
        container.style.setProperty("left", "0")
        container.style.setProperty("width", "100%")
        container.style.setProperty("height", "100%")
        container.style.setProperty("background-color", "#FFFFFF")
        container.style.setProperty("z-index", "99999")
        container.style.setProperty("display", "flex")
        container.style.setProperty("flex-direction", "column")
        container.style.setProperty("font-family", "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif")

        // 2. Create Top Bar div
        val topBar = document.createElement("div") as HTMLDivElement
        topBar.style.setProperty("height", "56px")
        topBar.style.setProperty("min-height", "56px")
        topBar.style.setProperty("display", "flex")
        topBar.style.setProperty("align-items", "center")
        topBar.style.setProperty("padding", "0 16px")
        topBar.style.setProperty("background-color", "#F8FAFC")
        topBar.style.setProperty("border-bottom", "1px solid #E2E8F0")
        topBar.style.setProperty("box-sizing", "border-box")

        // 2.1 Close button
        val closeBtn = document.createElement("button") as HTMLButtonElement
        closeBtn.style.setProperty("background", "none")
        closeBtn.style.setProperty("border", "none")
        closeBtn.style.setProperty("cursor", "pointer")
        closeBtn.style.setProperty("padding", "8px")
        closeBtn.style.setProperty("display", "flex")
        closeBtn.style.setProperty("align-items", "center")
        closeBtn.style.setProperty("justify-content", "center")
        closeBtn.style.setProperty("margin-right", "12px")
        closeBtn.style.setProperty("border-radius", "4px")
        closeBtn.innerHTML = """
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#0F172A" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
        """.trimIndent()
        
        closeBtn.addEventListener("click", { _ ->
            onDismiss()
        })

        // Hover effect for close button
        closeBtn.addEventListener("mouseenter", { _ ->
            closeBtn.style.setProperty("background-color", "#E2E8F0")
        })
        closeBtn.addEventListener("mouseleave", { _ ->
            closeBtn.style.setProperty("background-color", "transparent")
        })

        // 2.2 Title
        val titleText = document.createElement("span") as HTMLSpanElement
        titleText.style.setProperty("font-size", "18px")
        titleText.style.setProperty("font-weight", "600")
        titleText.style.setProperty("color", "#0F172A")
        titleText.style.setProperty("white-space", "nowrap")
        titleText.style.setProperty("overflow", "hidden")
        titleText.style.setProperty("text-overflow", "ellipsis")
        titleText.textContent = title

        topBar.appendChild(closeBtn)
        topBar.appendChild(titleText)

        // 3. Create IFrame
        val iframe = document.createElement("iframe") as HTMLIFrameElement
        iframe.src = url
        iframe.style.setProperty("flex", "1")
        iframe.style.setProperty("width", "100%")
        iframe.style.setProperty("height", "calc(100% - 56px)")
        iframe.style.setProperty("border", "none")
        iframe.style.setProperty("background-color", "#FFFFFF")

        container.appendChild(topBar)
        container.appendChild(iframe)

        document.body?.appendChild(container)

        onDispose {
            container.remove()
        }
    }
}
