package com.example.catlivewallpaper.render.assets

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.util.Log
import com.example.catlivewallpaper.R

class BitmapRepository(private val resources: Resources) {

    private var assetSet: AssetSet? = null

    fun loadAll(): AssetSet {
        if (assetSet != null) {
            Log.w(TAG, "loadAll() が既存アセット保持中に呼ばれた。旧アセットを解放して再ロード")
            clear()
        }
        return try {
            loadAllInternal(sampleSize = 1)
        } catch (e: OutOfMemoryError) {
            Log.w(TAG, "OOM 発生。inSampleSize=2 でリトライ", e)
            clear()
            try {
                loadAllInternal(sampleSize = 2)
            } catch (e2: OutOfMemoryError) {
                Log.e(TAG, "inSampleSize=2 でも OOM。初期化を中止", e2)
                clear()
                throw e2
            }
        }
    }

    fun clear() {
        assetSet?.let { assets ->
            assets.backgroundDay.recycle()
            assets.backgroundNight?.recycle()
            assets.catWalkFrames.forEach { it.recycle() }
            assets.catIdleFrame.recycle()
            assets.catPlayFrames.forEach { it.recycle() }
            assets.toyYarn.recycle()
        }
        assetSet = null
    }

    private fun loadAllInternal(sampleSize: Int): AssetSet {
        val opts = BitmapFactory.Options().apply { inSampleSize = sampleSize }

        val backgroundDay = requireNotNull(
            BitmapFactory.decodeResource(resources, R.drawable.background_room_day, opts)
        ) { "background_room_day の読み込みに失敗" }

        val backgroundNight = runCatching {
            BitmapFactory.decodeResource(resources, R.drawable.background_room_night, opts)
        }.onFailure { Log.w(TAG, "background_room_night の読み込みをスキップ", it) }.getOrNull()

        val catWalkFrames = listOf(
            requireNotNull(BitmapFactory.decodeResource(resources, R.drawable.cat_walk_1, opts)) {
                "cat_walk_1 の読み込みに失敗"
            },
            requireNotNull(BitmapFactory.decodeResource(resources, R.drawable.cat_walk_2, opts)) {
                "cat_walk_2 の読み込みに失敗"
            },
        )

        val catIdleFrame = requireNotNull(
            BitmapFactory.decodeResource(resources, R.drawable.cat_sit, opts)
        ) { "cat_sit の読み込みに失敗" }

        val catPlayFrames = listOf(
            requireNotNull(BitmapFactory.decodeResource(resources, R.drawable.cat_play_1, opts)) {
                "cat_play_1 の読み込みに失敗"
            },
            requireNotNull(BitmapFactory.decodeResource(resources, R.drawable.cat_play_2, opts)) {
                "cat_play_2 の読み込みに失敗"
            },
        )

        val toyYarn = requireNotNull(
            BitmapFactory.decodeResource(resources, R.drawable.toy_yarn, opts)
        ) { "toy_yarn の読み込みに失敗" }

        return AssetSet(
            backgroundDay = backgroundDay,
            backgroundNight = backgroundNight,
            catWalkFrames = catWalkFrames,
            catIdleFrame = catIdleFrame,
            catPlayFrames = catPlayFrames,
            toyYarn = toyYarn,
        ).also { assetSet = it }
    }

    private companion object {
        const val TAG = "BitmapRepository"
    }
}
