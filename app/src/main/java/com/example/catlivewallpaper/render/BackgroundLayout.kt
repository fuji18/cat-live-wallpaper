package com.example.catlivewallpaper.render

import android.graphics.Rect
import kotlin.math.max

class BackgroundLayout {
    private var surfaceWidth = 0
    private var surfaceHeight = 0
    private var bgWidth = 0
    private var bgHeight = 0

    fun updateViewport(surfaceWidth: Int, surfaceHeight: Int) {
        this.surfaceWidth = surfaceWidth
        this.surfaceHeight = surfaceHeight
    }

    fun updateBackground(bgWidth: Int, bgHeight: Int) {
        this.bgWidth = bgWidth
        this.bgHeight = bgHeight
    }

    fun resolveSourceRect(offsetX: Float): Rect {
        if (bgHeight == 0 || surfaceHeight == 0) return Rect(0, 0, bgWidth, bgHeight)
        val scale = surfaceHeight.toFloat() / bgHeight
        val scaledWidth = (bgWidth * scale).toInt()
        val sourceLeftScaled = resolveSourceLeft(scaledWidth, surfaceWidth, offsetX)
        val srcLeft = (sourceLeftScaled / scale).toInt().coerceIn(0, bgWidth)
        val srcWidth = (surfaceWidth / scale).toInt().coerceAtMost(bgWidth - srcLeft)
        return Rect(srcLeft, 0, srcLeft + srcWidth, bgHeight)
    }

    private fun resolveSourceLeft(scaledWidth: Int, surfaceWidth: Int, offsetX: Float): Int {
        val overflowWidth = max(0, scaledWidth - surfaceWidth)
        return (overflowWidth * offsetX.coerceIn(0f, 1f)).toInt()
    }
}
