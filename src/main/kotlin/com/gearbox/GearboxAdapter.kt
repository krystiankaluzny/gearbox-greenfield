package com.gearbox

import org.gearbox.external.Gearbox


class GearboxAdapter(private val gearbox: Gearbox) {

    private val objToState: Map<Any, GearboxState> = mapOf(
            1 as Any to GearboxState.DRIVE,
            2 as Any to GearboxState.PARK,
            3 as Any to GearboxState.REVERSE,
            4 as Any to GearboxState.NEUTRAL
    )

    fun getState(): GearboxState {
        val state = gearbox.state
        return objToState[state] ?: error("Unknown state: $state")
    }

    fun getCurrentGear(): Int = gearbox.currentGear as Int

    fun canIncreaseGear(): Boolean {
        val currentGear = getCurrentGear()
        val maxGear = gearbox.maxDrive
        return currentGear < maxGear
    }

    fun increaseGear() {
        if (canIncreaseGear()) {
            val currentGear = getCurrentGear()
            gearbox.setCurrentGear(currentGear + 1)
        }
    }

    fun canDecreaseGear(): Boolean {
        return getCurrentGear() > 1
    }

    fun decreaseGear() {
        if (canDecreaseGear()) {
            val currentGear = getCurrentGear()
            gearbox.setCurrentGear(currentGear - 1)
        }
    }
}