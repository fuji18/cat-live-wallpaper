package com.example.catlivewallpaper.render

import android.graphics.Rect
import android.view.SurfaceHolder
import com.example.catlivewallpaper.model.SceneState
import com.example.catlivewallpaper.render.assets.AssetSet

interface SceneRenderer {
    fun render(holder: SurfaceHolder, sceneState: SceneState, assets: AssetSet)
    fun updateViewport(width: Int, height: Int)
    fun resolveBackgroundRect(offsetX: Float): Rect
}
