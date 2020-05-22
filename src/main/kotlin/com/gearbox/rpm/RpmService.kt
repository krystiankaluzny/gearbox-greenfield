package com.gearbox.rpm

import org.gearbox.external.ExternalSystems

class RpmService(private val externalSystems: ExternalSystems) {

    fun getCurrentRpm() : RPM = RPM.of(externalSystems.currentRpm)
}