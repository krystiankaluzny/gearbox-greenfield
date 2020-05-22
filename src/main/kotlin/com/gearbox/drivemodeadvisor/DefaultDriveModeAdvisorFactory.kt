package com.gearbox.drivemodeadvisor

import com.gearbox.DriveMode
import com.gearbox.rpm.RPM
import com.gearbox.Threshold

interface DriveModeAdvisorFactory {
    fun getAdvisor(driveMode: DriveMode): DriveModeAdvisor
}

class DefaultDriveModeAdvisorFactory : DriveModeAdvisorFactory {

    private val characteristics = arrayOf(
            2000.0, 1000.0, //ECO
            1000.0, 0.5, 2500.0, 4500.0, // COMFORT
            1500.0, 0.5, 5000.0, 0.7, 5000.0, // SPORT
            5000.0, 1500.0, 2000.0, 3000.0, 6500.0, 14.0)
    private val aggressiveModeFactor = 1.3

    override fun getAdvisor(driveMode: DriveMode): DriveModeAdvisor {

        when (driveMode) {
            DriveMode.ECO -> {
                return EcoModeAdvisor(
                        minRpm = RPM.of(characteristics[0]),
                        maxRpm = RPM.of(characteristics[1]),
                        aggressiveModeFactor = aggressiveModeFactor)
            }
            DriveMode.COMFORT -> {

                return ComfortModeAdvisor(
                        maxThreshold = Threshold.ofNormalized(characteristics[3]),
                        minRpm = RPM.of(characteristics[2]),
                        maxRpm = RPM.of(characteristics[4]),
                        highRpm = RPM.of(characteristics[5]),
                        aggressiveModeFactor = aggressiveModeFactor)
            }
            DriveMode.SPORT -> {
                return SportModeAdvisor(
                        maxThreshold = Threshold.ofNormalized(characteristics[7]),
                        kickDownThreshold = Threshold.ofNormalized(characteristics[9]),
                        minRpm = RPM.of(characteristics[6]),
                        maxRpm = RPM.of(characteristics[8]),
                        kickDownMaxRpm = RPM.of(characteristics[10]),
                        aggressiveModeFactor = aggressiveModeFactor)
            }
        }
    }
}