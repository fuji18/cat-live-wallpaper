package com.example.catlivewallpaper.wallpaper

import android.service.wallpaper.WallpaperService
import com.example.catlivewallpaper.logic.CatBehaviorController
import com.example.catlivewallpaper.logic.FrameTicker
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
}
