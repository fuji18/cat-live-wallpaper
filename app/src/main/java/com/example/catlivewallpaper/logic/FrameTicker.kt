package com.example.catlivewallpaper.logic

import android.os.Handler
import com.example.catlivewallpaper.model.CatMode

class FrameTicker(private val handler: Handler) {

    private var pendingRunnable: Runnable? = null

    fun scheduleNext(mode: CatMode, onFrameRequested: () -> Unit) {
        cancel()
        val delay = intervalFor(mode)
        val runnable = Runnable { onFrameRequested() }
        pendingRunnable = runnable
        handler.postDelayed(runnable, delay)
    }

    fun cancel() {
        pendingRunnable?.let { handler.removeCallbacks(it) }
        pendingRunnable = null
    }

    fun intervalFor(mode: CatMode): Long = when (mode) {
        CatMode.WALK, CatMode.PLAY -> 67L
        CatMode.IDLE -> 500L
    }
}
