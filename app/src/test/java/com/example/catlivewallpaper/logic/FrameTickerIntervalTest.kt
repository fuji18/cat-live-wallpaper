package com.example.catlivewallpaper.logic

import android.os.Handler
import android.os.Looper
import com.example.catlivewallpaper.model.CatMode
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FrameTickerIntervalTest {

    private val ticker = FrameTicker(Handler(Looper.getMainLooper()))

    @Test
    fun `intervalFor WALK returns 67ms`() {
        assertEquals(67L, ticker.intervalFor(CatMode.WALK))
    }

    @Test
    fun `intervalFor PLAY returns 67ms`() {
        assertEquals(67L, ticker.intervalFor(CatMode.PLAY))
    }

    @Test
    fun `intervalFor IDLE returns 500ms`() {
        assertEquals(500L, ticker.intervalFor(CatMode.IDLE))
    }
}
