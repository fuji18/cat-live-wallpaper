package com.example.catlivewallpaper.wallpaper

import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import com.example.catlivewallpaper.logic.CatBehaviorControllerImpl
import com.example.catlivewallpaper.logic.FrameTicker
import com.example.catlivewallpaper.logic.TouchReactionControllerImpl
import com.example.catlivewallpaper.render.BackgroundLayout
import com.example.catlivewallpaper.render.SceneRendererImpl
import com.example.catlivewallpaper.render.SceneThemeResolver
import com.example.catlivewallpaper.render.assets.BitmapRepository

class CatWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        val frameTicker = FrameTicker(Handler(Looper.getMainLooper()))
        val bitmapRepository = BitmapRepository(resources)
        val renderer = SceneRendererImpl(BackgroundLayout(), SceneThemeResolver())
        val behaviorController = CatBehaviorControllerImpl()
        val touchReactionController = TouchReactionControllerImpl()
        return CatWallpaperEngine(
            behaviorController = behaviorController,
            touchReactionController = touchReactionController,
            renderer = renderer,
            frameTicker = frameTicker,
            bitmapRepository = bitmapRepository,
        )
    }
}
