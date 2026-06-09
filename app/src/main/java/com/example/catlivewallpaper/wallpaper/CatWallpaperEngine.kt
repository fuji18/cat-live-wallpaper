package com.example.catlivewallpaper.wallpaper

import android.os.SystemClock
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.example.catlivewallpaper.logic.CatBehaviorController
import com.example.catlivewallpaper.logic.FrameTicker
import com.example.catlivewallpaper.logic.TouchReactionController
import com.example.catlivewallpaper.model.SceneState
import com.example.catlivewallpaper.model.SceneTheme
import com.example.catlivewallpaper.model.ToySource
import com.example.catlivewallpaper.model.ToyState
import com.example.catlivewallpaper.orchestration.DrawFrameCoordinator
import com.example.catlivewallpaper.render.SceneRenderer
import com.example.catlivewallpaper.render.assets.BitmapRepository

private const val TAG = "CatWallpaperEngine"

class CatWallpaperEngine(
    private val behaviorController: CatBehaviorController,
    private val touchReactionController: TouchReactionController,
    private val renderer: SceneRenderer,
    private val frameTicker: FrameTicker,
    private val bitmapRepository: BitmapRepository,
) : WallpaperService.Engine() {

    private val coordinator = DrawFrameCoordinator(
        behaviorController = behaviorController,
        touchReactionController = touchReactionController,
        renderer = renderer,
        frameTicker = frameTicker,
    )

    private var isVisible = false
    private var surfaceWidth = 0
    private var surfaceHeight = 0

    override fun onTouchEvent(event: MotionEvent) {
        if (event.action != MotionEvent.ACTION_UP) return
        val tapStartMs = SystemClock.uptimeMillis()
        val x = event.x.coerceIn(0f, surfaceWidth.toFloat())
        val y = event.y.coerceIn(0f, surfaceHeight.toFloat())
        val updatedToy = touchReactionController.onTap(x, y, tapStartMs)
        coordinator.updateState { it.copy(toy = updatedToy) }
        Log.d(TAG, "tap_reaction_ms=${SystemClock.uptimeMillis() - tapStartMs}")
    }

    override fun onVisibilityChanged(visible: Boolean) {
        isVisible = visible
        coordinator.updateState { it.copy(isVisible = visible) }
        if (!visible) {
            coordinator.stop()
        } else {
            coordinator.drawFrame(SystemClock.uptimeMillis())
        }
    }

    override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        val startMs = SystemClock.uptimeMillis()
        surfaceWidth = width
        surfaceHeight = height
        coordinator.stop()
        renderer.updateViewport(width, height)
        val assets = try {
            bitmapRepository.loadAll()
        } catch (e: Exception) {
            Log.e(TAG, "asset_load_failed: ${e.message}")
            return
        }
        val initialCat = behaviorController.initialize(width, height)
        val initialState = SceneState(
            surfaceWidth = width,
            surfaceHeight = height,
            wallpaperOffsetX = 0f,
            isVisible = isVisible,
            theme = SceneTheme.DAY,
            cat = initialCat,
            toy = ToyState(
                isVisible = false,
                anchorX = 0f,
                anchorY = 0f,
                visibleUntilMs = 0L,
                source = ToySource.AUTO_PLAY,
            ),
        )
        coordinator.start(initialState, holder, assets)
        Log.d(TAG, "relayout_ms=${SystemClock.uptimeMillis() - startMs} w=$width h=$height")
    }

    override fun onOffsetsChanged(
        xOffset: Float,
        yOffset: Float,
        xOffsetStep: Float,
        yOffsetStep: Float,
        xPixelOffset: Int,
        yPixelOffset: Int,
    ) {
        coordinator.updateState {
            it.copy(wallpaperOffsetX = SceneState.normalizeOffset(xOffset))
        }
    }

    override fun onSurfaceDestroyed(holder: SurfaceHolder) {
        coordinator.stop()
        bitmapRepository.clear()
    }

    override fun onDestroy() {
        coordinator.stop()
        bitmapRepository.clear()
    }
}
