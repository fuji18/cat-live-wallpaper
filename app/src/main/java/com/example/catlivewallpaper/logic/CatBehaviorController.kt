package com.example.catlivewallpaper.logic

import com.example.catlivewallpaper.model.CatStateSnapshot
import com.example.catlivewallpaper.model.ToyState

interface CatBehaviorController {
    fun initialize(surfaceWidth: Int, surfaceHeight: Int): CatStateSnapshot
    fun update(nowMs: Long, current: CatStateSnapshot, toy: ToyState): CatStateSnapshot
    fun requestPlayAt(current: CatStateSnapshot, x: Float, y: Float, nowMs: Long): CatStateSnapshot
}
