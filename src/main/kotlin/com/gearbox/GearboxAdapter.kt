package com.gearbox

import org.gearbox.external.Gearbox


class GearboxAdapter (private val gearbox: Gearbox) {

    private val objToState: Map<Any, GearboxState> = mapOf(
            1 as Any to GearboxState.DRIVE,
            2 as Any to GearboxState.PARK,
            3 as Any to GearboxState.REVERSE,
            4 as Any to GearboxState.NEUTRAL
    )

    fun getState() : GearboxState {
        val state = gearbox.state
        return objToState[state] ?: error("Unknown state: $state")
    }

    fun getCurrentGear(): Int = gearbox.currentGear as Int

    fun increaseGear(): Boolean {
        val currentGear = getCurrentGear()
        val maxGear = gearbox.maxDrive

        if(currentGear < maxGear) {
            gearbox.setCurrentGear(currentGear + 1)
            return true
        }

        return false
    }

    fun decreaseGear(): Boolean {
        val currentGear = getCurrentGear()
        if(currentGear > 1) {
            gearbox.setCurrentGear(currentGear - 1)
            return true
        }

        return false
    }
}