package com.gearbox.drivemodeadvisor

import com.gearbox.AggressiveMode
import com.gearbox.rpm.RPM
import com.gearbox.Threshold

interface DriveModeAdvisor {

    fun shouldIncreaseGear(threshold: Threshold, currentRpm: RPM, aggressiveMode: AggressiveMode): Boolean
    fun shouldDecreaseGear(threshold: Threshold, currentRpm: RPM, aggressiveMode: AggressiveMode): Boolean
}