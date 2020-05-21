package com.gearbox

import com.gearbox.drivemodeadvisor.DriveModeAdvisorFactory
import com.gearbox.sound.SoundLevel
import com.gearbox.sound.SoundModule

class GearboxDriver(private val gearboxAdapter: GearboxAdapter,
                    private val driveModeAdvisorFactory: DriveModeAdvisorFactory,
                    private val soundModule: SoundModule) {

    private var driveMode = DriveMode.COMFORT
    private var aggressiveMode = AggressiveMode.LEVEL_1

    fun handleGas(threshold: Threshold) {
        if (isNotDrive()) {
            return
        }
        validateCurrentGear()

        val driveModeAdvisor = driveModeAdvisorFactory.getAdvisor(driveMode)

        val rpm = RPM.of(100.0)

        if(driveModeAdvisor.shouldIncreaseGear(threshold, rpm, aggressiveMode)) {
            if(gearboxAdapter.canIncreaseGear()){
                gearboxAdapter.increaseGear()
                if(shouldMakeSound(aggressiveMode)) {
                    soundModule.makeSound(SoundLevel.ofDecibel(40.0))
                }
            }

        } else if (driveModeAdvisor.shouldIncreaseGear(threshold, rpm, aggressiveMode)){
            gearboxAdapter.decreaseGear()
        }
    }

    private fun isNotDrive(): Boolean {
        return gearboxAdapter.getState() != GearboxState.DRIVE
    }

    private fun validateCurrentGear() {
        val currentGear = gearboxAdapter.getCurrentGear()
        if (currentGear <= 0) throw IllegalStateException("Invalid gear: $currentGear")
    }

    private fun shouldMakeSound(aggressiveMode: AggressiveMode): Boolean {
        return aggressiveMode == AggressiveMode.LEVEL_3
    }
}