package com.example.catlivewallpaper.render.assets

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.util.Log
import com.example.catlivewallpaper.R

class BitmapRepository(private val resources: Resources) {

    private var assetSet: AssetSet? = null

    fun loadAll(): AssetSet {
        val backgroundDay = requireNotNull(
            BitmapFactory.decodeResource(resources, R.drawable.background_room_day)
        ) { "background_room_day の読み込みに失敗" }

        val backgroundNight = runCatching {
            BitmapFactory.decodeResource(resources, R.drawable.background_room_night)
        }.onFailure { Log.w(TAG, "background_room_night の読み込みをスキップ", it) }.getOrNull()

        val catWalkFrames = listOf(
            requireNotNull(BitmapFactory.decodeResource(resources, R.drawable.cat_walk_1)) {
                "cat_walk_1 の読み込みに失敗"
            },
            requireNotNull(BitmapFactory.decodeResource(resources, R.drawable.cat_walk_2)) {
                "cat_walk_2 の読み込みに失敗"
            },
        )

        val catIdleFrame = requireNotNull(
            BitmapFactory.decodeResource(resources, R.drawable.cat_sit)
        ) { "cat_sit の読み込みに失敗" }

        val catPlayFrames = listOf(
            requireNotNull(BitmapFactory.decodeResource(resources, R.drawable.cat_play_1)) {
                "cat_play_1 の読み込みに失敗"
            },
            requireNotNull(BitmapFactory.decodeResource(resources, R.drawable.cat_play_2)) {
                "cat_play_2 の読み込みに失敗"
            },
        )

        val toyYarn = requireNotNull(
            BitmapFactory.decodeResource(resources, R.drawable.toy_yarn)
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
