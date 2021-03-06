package com.gearbox.rpm

// revolutions per minute
class RPM private constructor(private val value: Double) {
    companion object {

        fun of(rpm: Double): RPM {
            if (rpm < 0) throw IllegalArgumentException("RPM lower then 0")
            return RPM(rpm)
        }
    }

    operator fun compareTo(other: RPM): Int {
        return value.compareTo(other.value)
    }

    operator fun times (times: Double) : RPM {
        return of(value * times)
    }

    operator fun plus (rpm: RPM) : RPM {
        return of(value + rpm.value)
    }

    operator fun minus (rpm: RPM) : RPM {
        return of(value - rpm.value)
    }

    override fun toString(): String {
        return "RPM(value=$value)"
    }
}