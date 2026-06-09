package com.example.catlivewallpaper.wallpaper

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.service.wallpaper.WallpaperService
import android.util.Log
import com.example.catlivewallpaper.logic.CatBehaviorControllerImpl
import com.example.catlivewallpaper.logic.FrameTicker
import com.example.catlivewallpaper.logic.TouchReactionControllerImpl
import com.example.catlivewallpaper.render.BackgroundLayout
import com.example.catlivewallpaper.render.SceneRendererImpl
import com.example.catlivewallpaper.render.SceneThemeResolver
import com.example.catlivewallpaper.render.assets.BitmapRepository

private const val TAG = "CatWallpaperService"

class CatWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        val startMs = SystemClock.uptimeMillis()
        val frameTicker = FrameTicker(Handler(Looper.getMainLooper()))
        val bitmapRepository = BitmapRepository(resources)
        val renderer = SceneRendererImpl(BackgroundLayout(), SceneThemeResolver())
        val behaviorController = CatBehaviorControllerImpl()
        val touchReactionController = TouchReactionControllerImpl()
        val engine = CatWallpaperEngine(
            behaviorController = behaviorController,
            touchReactionController = touchReactionController,
            renderer = renderer,
            frameTicker = frameTicker,
            bitmapRepository = bitmapRepository,
        )
        Log.d(TAG, "engine_created_ms=${SystemClock.uptimeMillis() - startMs}")
        return engine
    }
}
