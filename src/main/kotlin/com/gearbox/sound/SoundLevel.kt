package com.gearbox.sound

class SoundLevel(private val powerLevel: Double) {

    companion object {
        fun ofDecibel(decibel: Double) = SoundLevel(decibel)
    }
}