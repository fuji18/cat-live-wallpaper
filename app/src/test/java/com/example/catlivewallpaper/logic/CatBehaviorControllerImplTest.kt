package com.example.catlivewallpaper.logic

import com.example.catlivewallpaper.model.CatMode
import com.example.catlivewallpaper.model.FacingDirection
import com.example.catlivewallpaper.model.ToySource
import com.example.catlivewallpaper.model.ToyState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class CatBehaviorControllerImplTest {

    private fun invisibleToy() = ToyState(false, 0f, 0f, 0L, ToySource.AUTO_PLAY)

    @Test
    fun `initialize creates IDLE state at center of surface`() {
        val controller = CatBehaviorControllerImpl()
        val state = controller.initialize(surfaceWidth = 1000, surfaceHeight = 2000)
        assertEquals(CatMode.IDLE, state.mode)
        assertEquals(500f, state.positionX)
        assertEquals(1400f, state.positionY)
        assertEquals(0f, state.velocityX)
    }

    @Test
    fun `update when state time remaining advances frame for WALK`() {
        val controller = CatBehaviorControllerImpl()
        val initial = controller.initialize(1000, 2000)
        val walkState = initial.copy(
            mode = CatMode.WALK,
            velocityX = 1f,
            stateStartedAtMs = 1000L,
            stateDurationMs = 5000L,
        )
        val updated = controller.update(nowMs = 2000L, current = walkState, toy = invisibleToy())
        assertEquals((walkState.frameIndex + 1) % 2, updated.frameIndex)
    }

    @Test
    fun `update when WALK state expires transitions to IDLE`() {
        val controller = CatBehaviorControllerImpl(random = Random(42))
        val initial = controller.initialize(1000, 2000)
        val walkState = initial.copy(
            mode = CatMode.WALK,
            stateStartedAtMs = 0L,
            stateDurationMs = 500L,
        )
        val updated = controller.update(nowMs = 1000L, current = walkState, toy = invisibleToy())
        assertEquals(CatMode.IDLE, updated.mode)
    }

    @Test
    fun `update when PLAY state expires transitions to IDLE`() {
        val controller = CatBehaviorControllerImpl(random = Random(0))
        val initial = controller.initialize(1000, 2000)
        val playState = initial.copy(
            mode = CatMode.PLAY,
            stateStartedAtMs = 0L,
            stateDurationMs = 100L,
        )
        val updated = controller.update(nowMs = 500L, current = playState, toy = invisibleToy())
        assertEquals(CatMode.IDLE, updated.mode)
    }

    @Test
    fun `update when play requested and not in PLAY starts PLAY state`() {
        val controller = CatBehaviorControllerImpl()
        val state = controller.initialize(1000, 2000).copy(
            mode = CatMode.WALK,
            stateStartedAtMs = 0L,
            stateDurationMs = 10000L,
        )
        val toy = ToyState(true, 500f, 700f, 99999L, ToySource.USER_TAP)
        val updated = controller.update(nowMs = 1000L, current = state, toy = toy)
        assertEquals(CatMode.PLAY, updated.mode)
        assertEquals(0, updated.frameIndex)
    }

    @Test
    fun `update when play requested but already in PLAY does not restart frameIndex`() {
        val controller = CatBehaviorControllerImpl()
        val state = controller.initialize(1000, 2000).copy(
            mode = CatMode.PLAY,
            frameIndex = 1,
            stateStartedAtMs = 0L,
            stateDurationMs = 10000L,
        )
        val toy = ToyState(true, 500f, 700f, 99999L, ToySource.USER_TAP)
        val updated = controller.update(nowMs = 1000L, current = state, toy = toy)
        assertEquals(CatMode.PLAY, updated.mode)
        assertEquals(0, updated.frameIndex)  // advanceFrame cycles: 1 -> 0
    }

    @Test
    fun `advanceFrame for IDLE does not change position`() {
        val controller = CatBehaviorControllerImpl()
        val state = controller.initialize(1000, 2000).copy(
            mode = CatMode.IDLE,
            positionX = 300f,
            stateStartedAtMs = 0L,
            stateDurationMs = 5000L,
        )
        val updated = controller.update(nowMs = 1000L, current = state, toy = invisibleToy())
        assertEquals(300f, updated.positionX)
    }

    @Test
    fun `advanceFrame for WALK clamps to right boundary and reverses velocity`() {
        val controller = CatBehaviorControllerImpl()
        val maxX = 1000 * 0.8f
        val state = controller.initialize(1000, 2000).copy(
            mode = CatMode.WALK,
            positionX = maxX - 0.5f,
            velocityX = 5f,
            stateStartedAtMs = 0L,
            stateDurationMs = 5000L,
        )
        val updated = controller.update(nowMs = 1000L, current = state, toy = invisibleToy())
        assertTrue(updated.positionX <= maxX)
        assertTrue(updated.velocityX < 0f)
        assertEquals(FacingDirection.LEFT, updated.facing)
    }

    @Test
    fun `advanceFrame for WALK clamps to left boundary and reverses velocity`() {
        val controller = CatBehaviorControllerImpl()
        val minX = 1000 * 0.2f
        val state = controller.initialize(1000, 2000).copy(
            mode = CatMode.WALK,
            positionX = minX + 0.5f,
            velocityX = -5f,
            stateStartedAtMs = 0L,
            stateDurationMs = 5000L,
        )
        val updated = controller.update(nowMs = 1000L, current = state, toy = invisibleToy())
        assertTrue(updated.positionX >= minX)
        assertTrue(updated.velocityX > 0f)
        assertEquals(FacingDirection.RIGHT, updated.facing)
    }
}
