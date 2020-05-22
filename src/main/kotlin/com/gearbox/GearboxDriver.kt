package com.gearbox

import com.gearbox.drivemodeadvisor.DriveModeAdvisorFactory
import com.gearbox.rpm.RpmService
import com.gearbox.sound.SoundLevel
import com.gearbox.sound.SoundModule

class GearboxDriver(private val gearboxAdapter: GearboxAdapter,
                    private val rpmService: RpmService,
                    private val driveModeAdvisorFactory: DriveModeAdvisorFactory,
                    private val soundModule: SoundModule) {

    private val doNothingResult = HandleGasResult(GearChangeInfo.none(), false)

    private var driveMode = DriveMode.COMFORT
    private var aggressiveMode = AggressiveMode.LEVEL_1

    fun handleGas(threshold: Threshold): HandleGasResult {
        if (isNotDrive()) {
            return doNothingResult
        }
        validateCurrentGear()

        val driveModeAdvisor = driveModeAdvisorFactory.getAdvisor(driveMode)

        val rpm = rpmService.getCurrentRpm()

        if (driveModeAdvisor.shouldIncreaseGear(threshold, rpm, aggressiveMode)) {
            if (gearboxAdapter.canIncreaseGear()) {
                gearboxAdapter.increaseGear()
                if (shouldMakeSound(aggressiveMode)) {
                    soundModule.makeSound(SoundLevel.ofDecibel(40.0))

                    return HandleGasResult.withSound(GearChangeInfo.increasedBy(1))
                }

                return HandleGasResult.withoutSound(GearChangeInfo.increasedBy(1))
            }

        } else if (driveModeAdvisor.shouldDecreaseGear(threshold, rpm, aggressiveMode)) {
            if (gearboxAdapter.canDecreaseGear()) {
                gearboxAdapter.decreaseGear()

                return HandleGasResult.withoutSound(GearChangeInfo.decreasedBy(1))
            }
        }

        return doNothingResult
    }

    fun changeDriveMode(driveMode: DriveMode) {
        this.driveMode = driveMode
    }

    fun changeAggressiveMode(aggressiveMode: AggressiveMode) {
        this.aggressiveMode = aggressiveMode
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