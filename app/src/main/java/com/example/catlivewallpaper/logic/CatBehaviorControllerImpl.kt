package com.example.catlivewallpaper.logic

import com.example.catlivewallpaper.model.CatMode
import com.example.catlivewallpaper.model.CatStateSnapshot
import com.example.catlivewallpaper.model.FacingDirection
import com.example.catlivewallpaper.model.ToySource
import com.example.catlivewallpaper.model.ToyState
import kotlin.random.Random

class CatBehaviorControllerImpl(
    private val random: Random = Random.Default,
) : CatBehaviorController {

    private var surfaceWidth = 0
    private var surfaceHeight = 0

    override fun initialize(surfaceWidth: Int, surfaceHeight: Int): CatStateSnapshot {
        this.surfaceWidth = surfaceWidth
        this.surfaceHeight = surfaceHeight
        return CatStateSnapshot(
            mode = CatMode.IDLE,
            facing = FacingDirection.RIGHT,
            positionX = surfaceWidth * 0.5f,
            positionY = surfaceHeight * 0.7f,
            velocityX = 0f,
            frameIndex = 0,
            stateStartedAtMs = 0L,
            stateDurationMs = 2000L,
        )
    }

    override fun update(nowMs: Long, current: CatStateSnapshot, toy: ToyState): CatStateSnapshot {
        val isPlayRequested = toy.isVisible && toy.source == ToySource.USER_TAP
        val stateEndsAt = current.stateStartedAtMs + current.stateDurationMs

        if (!isPlayRequested && nowMs < stateEndsAt) {
            return advanceFrame(current)
        }
        if (isPlayRequested) {
            return startPlayState(current, toy.anchorX, nowMs)
        }
        return selectNextState(current, nowMs)
    }

    override fun requestPlayAt(
        current: CatStateSnapshot,
        x: Float,
        y: Float,
        nowMs: Long,
    ): CatStateSnapshot = startPlayState(current, x, nowMs)

    private fun advanceFrame(cat: CatStateSnapshot): CatStateSnapshot {
        if (cat.mode == CatMode.IDLE) return cat
        val rawX = cat.positionX + cat.velocityX
        val minX = surfaceWidth * 0.2f
        val maxX = surfaceWidth * 0.8f
        val clampedX = rawX.coerceIn(minX, maxX)
        val hitBoundary = rawX != clampedX
        val newVx = if (hitBoundary) -cat.velocityX else cat.velocityX
        val newFacing = when {
            newVx > 0f -> FacingDirection.RIGHT
            newVx < 0f -> FacingDirection.LEFT
            else -> cat.facing
        }
        return cat.copy(
            frameIndex = (cat.frameIndex + 1) % 2,
            positionX = clampedX,
            velocityX = newVx,
            facing = newFacing,
        )
    }

    private fun startPlayState(cat: CatStateSnapshot, anchorX: Float, nowMs: Long): CatStateSnapshot {
        val duration = 1500L + (random.nextFloat() * 1500).toLong()
        return cat.copy(
            mode = CatMode.PLAY,
            velocityX = 0f,
            frameIndex = 0,
            stateStartedAtMs = nowMs,
            stateDurationMs = duration,
        )
    }

    private fun selectNextState(cat: CatStateSnapshot, nowMs: Long): CatStateSnapshot {
        val nextMode = when (cat.mode) {
            CatMode.WALK -> CatMode.IDLE
            CatMode.IDLE -> if (random.nextFloat() < 0.3f) CatMode.PLAY else CatMode.WALK
            CatMode.PLAY -> CatMode.IDLE
        }
        return startState(cat, nextMode, nowMs)
    }

    private fun startState(cat: CatStateSnapshot, mode: CatMode, nowMs: Long): CatStateSnapshot {
        val duration = when (mode) {
            CatMode.WALK -> 2500L + (random.nextFloat() * 2500).toLong()
            CatMode.IDLE -> 2000L + (random.nextFloat() * 2000).toLong()
            CatMode.PLAY -> 1500L + (random.nextFloat() * 1500).toLong()
        }
        val (facing, velocityX) = when (mode) {
            CatMode.WALK -> {
                val dir = if (random.nextBoolean()) FacingDirection.RIGHT else FacingDirection.LEFT
                val speed = surfaceWidth * 0.001f
                val vx = if (dir == FacingDirection.RIGHT) speed else -speed
                dir to vx
            }
            else -> cat.facing to 0f
        }
        return cat.copy(
            mode = mode,
            facing = facing,
            velocityX = velocityX,
            frameIndex = 0,
            stateStartedAtMs = nowMs,
            stateDurationMs = duration,
        )
    }
}
