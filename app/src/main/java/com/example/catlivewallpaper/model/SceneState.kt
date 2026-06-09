package com.example.catlivewallpaper.model

data class SceneState(
    val surfaceWidth: Int,
    val surfaceHeight: Int,
    val wallpaperOffsetX: Float,
    val isVisible: Boolean,
    val theme: SceneTheme,
    val cat: CatStateSnapshot,
    val toy: ToyState,
) {
    companion object {
        fun normalizeOffset(offset: Float): Float = offset.coerceIn(0f, 1f)
    }
}
