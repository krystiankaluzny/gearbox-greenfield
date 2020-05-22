package com.gearbox.drivemodeadvisor

import com.gearbox.AggressiveMode
import com.gearbox.rpm.RPM
import com.gearbox.Threshold

class ComfortModeAdvisor(private val maxThreshold: Threshold,
                         private val minRpm: RPM,
                         private val maxRpm: RPM,
                         private val highRpm: RPM,
                         private val aggressiveModeFactor: Double)
    : DriveModeAdvisor {

    override fun shouldIncreaseGear(threshold: Threshold, currentRpm: RPM, aggressiveMode: AggressiveMode): Boolean {

        if (threshold >= maxThreshold) return false

        if (aggressiveMode == AggressiveMode.LEVEL_1) {
            return currentRpm > maxRpm
        }

        return currentRpm > aggressiveMaxRpm()
    }

    override fun shouldDecreaseGear(threshold: Threshold, currentRpm: RPM, aggressiveMode: AggressiveMode): Boolean {
        if (currentRpm < minRpm) return true

        if (threshold < maxThreshold && aggressiveMode == AggressiveMode.LEVEL_3) {
            //rekukcja bo TIR
            if (currentRpm <= aggressiveMaxRpm() && currentRpm < highRpm) {
                return true
            }
        }
        return false
    }

    private fun aggressiveMaxRpm() = maxRpm * aggressiveModeFactor
}