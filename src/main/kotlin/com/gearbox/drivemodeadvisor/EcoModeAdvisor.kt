package com.gearbox.drivemodeadvisor

import com.gearbox.AggressiveMode
import com.gearbox.rpm.RPM
import com.gearbox.Threshold

class EcoModeAdvisor(private val minRpm: RPM,
                     private val maxRpm: RPM,
                     private val aggressiveModeFactor: Double) : DriveModeAdvisor {

    override fun shouldIncreaseGear(threshold: Threshold, currentRpm: RPM, aggressiveMode: AggressiveMode): Boolean {
        if (aggressiveMode == AggressiveMode.LEVEL_1) {
            return currentRpm > maxRpm
        }

        return currentRpm > maxRpm * aggressiveModeFactor
    }

    override fun shouldDecreaseGear(threshold: Threshold, currentRpm: RPM, aggressiveMode: AggressiveMode): Boolean {
        return currentRpm < minRpm
    }
}