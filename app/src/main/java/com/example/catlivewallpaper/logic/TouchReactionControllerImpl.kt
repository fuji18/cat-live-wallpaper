package com.example.catlivewallpaper.logic

import com.example.catlivewallpaper.model.ToySource
import com.example.catlivewallpaper.model.ToyState

class TouchReactionControllerImpl : TouchReactionController {

    companion object {
        private const val DISPLAY_DURATION_MS = 2000L
    }

    override fun onTap(x: Float, y: Float, nowMs: Long): ToyState = ToyState(
        isVisible = true,
        anchorX = x,
        anchorY = y,
        visibleUntilMs = nowMs + DISPLAY_DURATION_MS,
        source = ToySource.USER_TAP,
    )

    override fun update(nowMs: Long, current: ToyState): ToyState {
        if (!current.isVisible) return current
        return if (nowMs >= current.visibleUntilMs) current.copy(isVisible = false) else current
    }
}
