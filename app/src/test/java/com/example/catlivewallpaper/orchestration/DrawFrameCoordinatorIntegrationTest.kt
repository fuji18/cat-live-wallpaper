package com.example.catlivewallpaper.orchestration

import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import com.example.catlivewallpaper.logic.CatBehaviorControllerImpl
import com.example.catlivewallpaper.logic.FrameTicker
import com.example.catlivewallpaper.logic.TouchReactionControllerImpl
import com.example.catlivewallpaper.model.CatMode
import com.example.catlivewallpaper.model.SceneState
import com.example.catlivewallpaper.model.SceneTheme
import com.example.catlivewallpaper.model.ToySource
import com.example.catlivewallpaper.model.ToyState
import com.example.catlivewallpaper.render.SceneRenderer
import com.example.catlivewallpaper.render.assets.AssetSet
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DrawFrameCoordinatorIntegrationTest {

    private lateinit var coordinator: DrawFrameCoordinator
    private lateinit var fakeRenderer: FakeSceneRenderer
    private lateinit var mockHolder: SurfaceHolder
    private lateinit var testAssets: AssetSet

    @Before
    fun setup() {
        fakeRenderer = FakeSceneRenderer()
        mockHolder = mock()
        val handler = Handler(Looper.getMainLooper())
        val ticker = FrameTicker(handler)
        coordinator = DrawFrameCoordinator(
            behaviorController = CatBehaviorControllerImpl(),
            touchReactionController = TouchReactionControllerImpl(),
            renderer = fakeRenderer,
            frameTicker = ticker,
        )
        testAssets = createTestAssets()
    }

    @Test
    fun `isStarted is false before start`() {
        assertFalse(coordinator.isStarted)
    }

    @Test
    fun `isStarted is true after start`() {
        val initial = createInitialState()
        coordinator.start(initial, mockHolder, testAssets)
        assertTrue(coordinator.isStarted)
    }

    @Test
    fun `start triggers initial drawFrame and renders`() {
        val initial = createInitialState(isVisible = true)
        coordinator.start(initial, mockHolder, testAssets)
        assertEquals(1, fakeRenderer.renderCallCount)
    }

    @Test
    fun `start with isVisible false does not render`() {
        val initial = createInitialState(isVisible = false)
        coordinator.start(initial, mockHolder, testAssets)
        assertEquals(0, fakeRenderer.renderCallCount)
    }

    @Test
    fun `drawFrame updates cat state`() {
        val initial = createInitialState(isVisible = true).let { s ->
            s.copy(cat = s.cat.copy(
                mode = CatMode.WALK,
                velocityX = 1f,
                stateStartedAtMs = 0L,
                stateDurationMs = 10000L,
            ))
        }
        coordinator.start(initial, mockHolder, testAssets)
        val firstFrameIndex = fakeRenderer.lastRenderedState!!.cat.frameIndex
        coordinator.drawFrame(nowMs = 1000L)
        val secondFrameIndex = fakeRenderer.lastRenderedState!!.cat.frameIndex
        assertEquals((firstFrameIndex + 1) % 2, secondFrameIndex)
    }

    @Test
    fun `updateState reflects offset change`() {
        val initial = createInitialState(isVisible = true)
        coordinator.start(initial, mockHolder, testAssets)
        coordinator.updateState { it.copy(wallpaperOffsetX = 0.75f) }
        coordinator.drawFrame(nowMs = 100L)
        assertEquals(0.75f, fakeRenderer.lastRenderedState!!.wallpaperOffsetX)
    }

    @Test
    fun `toy update propagates through drawFrame`() {
        val initial = createInitialState(isVisible = true)
        coordinator.start(initial, mockHolder, testAssets)
        val activeToy = ToyState(true, 300f, 400f, 99999L, ToySource.USER_TAP)
        coordinator.updateState { it.copy(toy = activeToy) }
        coordinator.drawFrame(nowMs = 100L)
        assertTrue(fakeRenderer.lastRenderedState!!.toy.isVisible)
    }

    @Test
    fun `toy expires after visibleUntilMs and becomes invisible`() {
        val initial = createInitialState(isVisible = true)
        coordinator.start(initial, mockHolder, testAssets)
        val toySetAt0 = ToyState(true, 300f, 400f, 1000L, ToySource.USER_TAP)
        coordinator.updateState { it.copy(toy = toySetAt0) }
        coordinator.drawFrame(nowMs = 2000L)  // past visibleUntilMs=1000
        assertFalse(fakeRenderer.lastRenderedState!!.toy.isVisible)
    }

    @Test
    fun `stop cancels frame loop and invalidate clears state`() {
        val initial = createInitialState(isVisible = true)
        coordinator.start(initial, mockHolder, testAssets)
        assertTrue(coordinator.isStarted)
        coordinator.stop()
        coordinator.invalidate()
        assertFalse(coordinator.isStarted)
    }

    // Helpers

    private fun createInitialState(isVisible: Boolean = true): SceneState {
        val behaviorController = CatBehaviorControllerImpl()
        val cat = behaviorController.initialize(1000, 2000)
        return SceneState(
            surfaceWidth = 1000,
            surfaceHeight = 2000,
            wallpaperOffsetX = 0f,
            isVisible = isVisible,
            theme = SceneTheme.DAY,
            cat = cat,
            toy = ToyState(false, 0f, 0f, 0L, ToySource.AUTO_PLAY),
        )
    }

    private fun createTestAssets(): AssetSet {
        val blank = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        return AssetSet(
            backgroundDay = blank,
            backgroundNight = null,
            catWalkFrames = listOf(blank, blank),
            catIdleFrame = blank,
            catPlayFrames = listOf(blank, blank),
            toyYarn = blank,
        )
    }
}

private class FakeSceneRenderer : SceneRenderer {
    var renderCallCount = 0
    var lastRenderedState: SceneState? = null

    override fun render(holder: SurfaceHolder, sceneState: SceneState, assets: AssetSet) {
        renderCallCount++
        lastRenderedState = sceneState
    }

    override fun updateViewport(width: Int, height: Int) {}

    override fun resolveBackgroundRect(offsetX: Float): Rect = Rect(0, 0, 1, 1)
}
