package com.example.catlivewallpaper.orchestration

import android.os.SystemClock
import android.view.SurfaceHolder
import com.example.catlivewallpaper.logic.CatBehaviorController
import com.example.catlivewallpaper.logic.FrameTicker
import com.example.catlivewallpaper.model.SceneState
import com.example.catlivewallpaper.render.SceneRenderer
import com.example.catlivewallpaper.render.assets.AssetSet

class DrawFrameCoordinator(
    private val behaviorController: CatBehaviorController,
    private val renderer: SceneRenderer,
    private val frameTicker: FrameTicker,
) {

    private var sceneState: SceneState? = null
    private var holder: SurfaceHolder? = null
    private var assets: AssetSet? = null

    fun start(initialState: SceneState, holder: SurfaceHolder, assets: AssetSet) {
        this.sceneState = initialState
        this.holder = holder
        this.assets = assets
        drawFrame(SystemClock.uptimeMillis())
    }

    fun stop() {
        frameTicker.cancel()
    }

    fun updateState(transform: (SceneState) -> SceneState) {
        sceneState = sceneState?.let(transform)
    }

    fun drawFrame(nowMs: Long) {
        val state = sceneState ?: return
        if (!state.isVisible) return
        val currentHolder = holder ?: return
        val currentAssets = assets ?: return

        val updatedCat = behaviorController.update(nowMs, state.cat, state.toy)
        val updatedState = state.copy(cat = updatedCat)
        sceneState = updatedState

        renderer.render(currentHolder, updatedState, currentAssets)
        frameTicker.scheduleNext(updatedCat.mode) { drawFrame(SystemClock.uptimeMillis()) }
    }
}
