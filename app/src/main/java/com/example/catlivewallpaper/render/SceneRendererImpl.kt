package com.example.catlivewallpaper.render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import com.example.catlivewallpaper.model.CatMode
import com.example.catlivewallpaper.model.FacingDirection
import com.example.catlivewallpaper.model.SceneState
import com.example.catlivewallpaper.render.assets.AssetSet

class SceneRendererImpl(
    private val backgroundLayout: BackgroundLayout,
    private val themeResolver: SceneThemeResolver,
) : SceneRenderer {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val flipMatrix = Matrix()
    private val screenRect = Rect()

    override fun updateViewport(width: Int, height: Int) {
        backgroundLayout.updateViewport(width, height)
        screenRect.set(0, 0, width, height)
    }

    override fun resolveBackgroundRect(offsetX: Float): Rect =
        backgroundLayout.resolveSourceRect(offsetX)

    override fun render(holder: SurfaceHolder, sceneState: SceneState, assets: AssetSet) {
        val canvas = holder.lockCanvas() ?: return
        try {
            drawBackground(canvas, sceneState, assets)
            drawCat(canvas, sceneState, assets)
            if (sceneState.toy.isVisible) {
                drawToy(canvas, sceneState, assets)
            }
        } finally {
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawBackground(canvas: Canvas, sceneState: SceneState, assets: AssetSet) {
        val background = themeResolver.resolveBackground(sceneState.theme, assets)
        backgroundLayout.updateBackground(background.width, background.height)
        val srcRect = backgroundLayout.resolveSourceRect(sceneState.wallpaperOffsetX)
        canvas.drawBitmap(background, srcRect, screenRect, null)
    }

    private fun drawCat(canvas: Canvas, sceneState: SceneState, assets: AssetSet) {
        val cat = sceneState.cat
        val sprite = resolveCatSprite(cat.mode, cat.frameIndex, assets)
        val drawX = cat.positionX - sprite.width / 2f
        val drawY = cat.positionY - sprite.height / 2f
        if (cat.facing == FacingDirection.LEFT) {
            flipMatrix.setScale(-1f, 1f, cat.positionX, cat.positionY)
            canvas.save()
            canvas.concat(flipMatrix)
        }
        canvas.drawBitmap(sprite, drawX, drawY, paint)
        if (cat.facing == FacingDirection.LEFT) {
            canvas.restore()
        }
    }

    private fun drawToy(canvas: Canvas, sceneState: SceneState, assets: AssetSet) {
        val toy = sceneState.toy
        val bitmap = assets.toyYarn
        canvas.drawBitmap(
            bitmap,
            toy.anchorX - bitmap.width / 2f,
            toy.anchorY - bitmap.height / 2f,
            paint,
        )
    }

    private fun resolveCatSprite(mode: CatMode, frameIndex: Int, assets: AssetSet): Bitmap =
        when (mode) {
            CatMode.WALK -> assets.catWalkFrames.getOrElse(frameIndex % 2) { assets.catIdleFrame }
            CatMode.IDLE -> assets.catIdleFrame
            CatMode.PLAY -> assets.catPlayFrames.getOrElse(frameIndex % 2) { assets.catIdleFrame }
        }
}
