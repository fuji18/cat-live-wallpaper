package com.example.catlivewallpaper.model

data class CatStateSnapshot(
    val mode: CatMode,
    val facing: FacingDirection,
    val positionX: Float,
    val positionY: Float,
    val velocityX: Float,
    val frameIndex: Int,
    val stateStartedAtMs: Long,
    val stateDurationMs: Long,
)

enum class CatMode {
    WALK,
    IDLE,
    PLAY,
}

enum class FacingDirection {
    LEFT,
    RIGHT,
}
