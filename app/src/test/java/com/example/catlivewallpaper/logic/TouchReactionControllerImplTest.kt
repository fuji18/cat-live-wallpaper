package com.example.catlivewallpaper.logic

import com.example.catlivewallpaper.model.ToySource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TouchReactionControllerImplTest {

    private val controller = TouchReactionControllerImpl()

    @Test
    fun `onTap creates visible ToyState at tap position`() {
        val toy = controller.onTap(x = 300f, y = 600f, nowMs = 1000L)
        assertTrue(toy.isVisible)
        assertEquals(300f, toy.anchorX)
        assertEquals(600f, toy.anchorY)
        assertEquals(ToySource.USER_TAP, toy.source)
    }

    @Test
    fun `onTap sets visibleUntilMs 2000ms after nowMs`() {
        val toy = controller.onTap(x = 0f, y = 0f, nowMs = 5000L)
        assertEquals(7000L, toy.visibleUntilMs)
    }

    @Test
    fun `update when toy is invisible returns same state`() {
        val invisible = controller.onTap(0f, 0f, 1000L).copy(isVisible = false)
        val result = controller.update(nowMs = 500L, current = invisible)
        assertFalse(result.isVisible)
        assertEquals(invisible, result)
    }

    @Test
    fun `update before expiry returns unchanged visible state`() {
        val toy = controller.onTap(0f, 0f, nowMs = 1000L)  // visibleUntilMs = 3000
        val result = controller.update(nowMs = 2999L, current = toy)
        assertTrue(result.isVisible)
    }

    @Test
    fun `update at exact expiry returns invisible state`() {
        val toy = controller.onTap(0f, 0f, nowMs = 1000L)  // visibleUntilMs = 3000
        val result = controller.update(nowMs = 3000L, current = toy)
        assertFalse(result.isVisible)
    }

    @Test
    fun `update past expiry returns invisible state`() {
        val toy = controller.onTap(0f, 0f, nowMs = 1000L)  // visibleUntilMs = 3000
        val result = controller.update(nowMs = 9000L, current = toy)
        assertFalse(result.isVisible)
    }
}
