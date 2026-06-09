package com.example.catlivewallpaper.render

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BackgroundLayoutTest {

    private val layout = BackgroundLayout()

    @Before
    fun setup() {
        // surface: 1000x2000, background: 3000x2000
        // scale = 2000/2000 = 1.0, scaledBgWidth = 3000
        // overflowWidth = 3000 - 1000 = 2000
        layout.updateViewport(surfaceWidth = 1000, surfaceHeight = 2000)
        layout.updateBackground(bgWidth = 3000, bgHeight = 2000)
    }

    @Test
    fun `resolveSourceRect with offsetX 0 starts at left`() {
        val rect = layout.resolveSourceRect(offsetX = 0f)
        assertEquals(0, rect.left)
    }

    @Test
    fun `resolveSourceRect with offsetX 1 starts at rightmost position`() {
        val rectAt0 = layout.resolveSourceRect(offsetX = 0f)
        val rectAt1 = layout.resolveSourceRect(offsetX = 1f)
        assertTrue("offsetX=1 should start further right than offsetX=0", rectAt1.left > rectAt0.left)
    }

    @Test
    fun `resolveSourceRect with offsetX 0_5 is between left and right`() {
        val leftAt0 = layout.resolveSourceRect(offsetX = 0f).left
        val leftAt1 = layout.resolveSourceRect(offsetX = 1f).left
        val leftAt05 = layout.resolveSourceRect(offsetX = 0.5f).left
        assertTrue("offsetX=0.5 should be right of offsetX=0", leftAt05 > leftAt0)
        assertTrue("offsetX=0.5 should be left of offsetX=1", leftAt05 < leftAt1)
    }

    @Test
    fun `resolveSourceRect right does not exceed background width`() {
        val rect = layout.resolveSourceRect(offsetX = 1f)
        assertTrue("right=${rect.right} must not exceed bgWidth=3000", rect.right <= 3000)
    }

    @Test
    fun `resolveSourceRect top is always 0`() {
        assertEquals(0, layout.resolveSourceRect(offsetX = 0f).top)
        assertEquals(0, layout.resolveSourceRect(offsetX = 0.5f).top)
        assertEquals(0, layout.resolveSourceRect(offsetX = 1f).top)
    }

    @Test
    fun `resolveSourceRect bottom equals background height`() {
        val rect = layout.resolveSourceRect(offsetX = 0f)
        assertEquals(2000, rect.bottom)
    }
}
