package com.example.catlivewallpaper.model

data class ToyState(
    val isVisible: Boolean,
    val anchorX: Float,
    val anchorY: Float,
    val visibleUntilMs: Long,
    val source: ToySource,
)

enum class ToySource {
    USER_TAP,
    AUTO_PLAY,
}
