package com.gearbox

class Threshold private constructor(private val value: Double) {

    companion object {

        fun ofPercentage(threshold: Double): Threshold {
            if (threshold < 0) throw IllegalArgumentException("Threshold lower then 0")
            if (threshold > 100) throw IllegalArgumentException("Threshold greater then 100")
            return Threshold(threshold / 100.0)
        }

        fun ofNormalized(threshold: Double): Threshold {
            if (threshold < 0.0) throw IllegalArgumentException("Threshold lower then 0.0")
            if (threshold > 1.0) throw IllegalArgumentException("Threshold greater then 1.0")
            return Threshold(threshold)
        }
    }

    operator fun compareTo(other: Threshold): Int {
        return value.compareTo(other.value)
    }
}