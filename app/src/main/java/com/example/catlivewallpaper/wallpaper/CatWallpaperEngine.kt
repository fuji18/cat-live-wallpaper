package com.example.catlivewallpaper.wallpaper

import android.os.SystemClock
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.example.catlivewallpaper.logic.CatBehaviorController
import com.example.catlivewallpaper.logic.FrameTicker
import com.example.catlivewallpaper.model.SceneState
import com.example.catlivewallpaper.model.SceneTheme
import com.example.catlivewallpaper.model.ToySource
import com.example.catlivewallpaper.model.ToyState
import com.example.catlivewallpaper.orchestration.DrawFrameCoordinator
import com.example.catlivewallpaper.render.SceneRenderer
import com.example.catlivewallpaper.render.assets.BitmapRepository

class CatWallpaperEngine(
    private val behaviorController: CatBehaviorController,
    private val renderer: SceneRenderer,
    private val frameTicker: FrameTicker,
    private val bitmapRepository: BitmapRepository,
) : WallpaperService.Engine() {

    private val coordinator = DrawFrameCoordinator(
        behaviorController = behaviorController,
        renderer = renderer,
        frameTicker = frameTicker,
    )

    private var isVisible = false

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
        coordinator.stop()
        renderer.updateViewport(width, height)
        val assets = bitmapRepository.loadAll()
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
