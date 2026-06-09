package com.example.catlivewallpaper.render

import android.graphics.Bitmap
import com.example.catlivewallpaper.model.SceneTheme
import com.example.catlivewallpaper.render.assets.AssetSet

class SceneThemeResolver {
    fun resolveTheme(): SceneTheme = SceneTheme.DAY

    fun resolveBackground(theme: SceneTheme, assets: AssetSet): Bitmap =
        when (theme) {
            SceneTheme.DAY -> assets.backgroundDay
            SceneTheme.NIGHT -> assets.backgroundNight ?: assets.backgroundDay
        }
}
