package com.gearbox

import com.gearbox.drivemodeadvisor.DriveModeAdvisorFactory

class GearboxDriver(private val gearboxAdapter: GearboxAdapter,
                    private val driveModeAdvisorFactory: DriveModeAdvisorFactory) {

    var driveMode = DriveMode.COMFORT
    var aggressiveMode = AggressiveMode.LEVEL_1

    fun handleGas(threshold: Threshold) {
        if (isNotDrive()) {
            return
        }
        validateCurrentGear()

        val driveModeAdvisor = driveModeAdvisorFactory.getAdvisor(driveMode)

        val rpm = RPM.of(100.0)

        if(driveModeAdvisor.shouldIncreaseGear(threshold, rpm, aggressiveMode)) {
            val increased = gearboxAdapter.increaseGear()
            if(aggressiveMode == AggressiveMode.LEVEL_3 && increased) {
                //sound
            }
        } else if (driveModeAdvisor.shouldIncreaseGear(threshold, rpm, aggressiveMode)){
            val decreased = gearboxAdapter.decreaseGear()


        }
    }

    private fun isNotDrive(): Boolean {
        return gearboxAdapter.getState() != GearboxState.DRIVE
    }

    private fun validateCurrentGear() {
        val currentGear = gearboxAdapter.getCurrentGear()
        if (currentGear <= 0) throw IllegalStateException("Invalid gear: $currentGear")
    }
}