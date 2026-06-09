package com.example.catlivewallpaper.logic

import com.example.catlivewallpaper.model.ToyState

interface TouchReactionController {
    fun onTap(x: Float, y: Float, nowMs: Long): ToyState
    fun update(nowMs: Long, current: ToyState): ToyState
}
