package com.example.catlivewallpaper.render.assets

import android.graphics.Bitmap

data class AssetSet(
    val backgroundDay: Bitmap,
    val backgroundNight: Bitmap?,
    val catWalkFrames: List<Bitmap>,
    val catIdleFrame: Bitmap,
    val catPlayFrames: List<Bitmap>,
    val toyYarn: Bitmap,
)
