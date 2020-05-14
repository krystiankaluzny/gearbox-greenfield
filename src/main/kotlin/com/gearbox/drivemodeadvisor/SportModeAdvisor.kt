package com.gearbox.drivemodeadvisor

import com.gearbox.AggressiveMode
import com.gearbox.RPM
import com.gearbox.Threshold

class SportModeAdvisor(private val maxThreshold: Threshold,
                       private val kickDownThreshold: Threshold,
                       private val minRpm: RPM,
                       private val maxRpm: RPM,
                       private val aggressiveModeFactor: Double,
                       private val kickDownMaxRpm: RPM) : DriveModeAdvisor {

    override fun shouldIncreaseGear(threshold: Threshold, currentRpm: RPM, aggressiveMode: AggressiveMode): Boolean {

        if (threshold < maxThreshold) {

            if (aggressiveMode == AggressiveMode.LEVEL_1) {
                return currentRpm > maxRpm
            }

            return currentRpm > maxRpm * aggressiveModeFactor
        }

        return false
    }

    override fun shouldDecreaseGear(threshold: Threshold, currentRpm: RPM, aggressiveMode: AggressiveMode): Boolean {

        if (currentRpm < minRpm) return true

        if (threshold < kickDownThreshold) {

            return currentRpm < kickDownMaxRpm
        }

        return false
    }
}