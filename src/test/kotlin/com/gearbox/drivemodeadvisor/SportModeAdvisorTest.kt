package com.gearbox.drivemodeadvisor

import com.gearbox.AggressiveMode
import com.gearbox.Threshold
import com.gearbox.rpm.RPM
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SportModeAdvisorTest {
    private val oneRPM = RPM.of(1.0)

    private val threshold = Threshold.ofPercentage(50.0)
    private val minRpm = RPM.of(1000.0)
    private val maxRpm = RPM.of(2000.0)
    private val kickDownRpm = RPM.of(3000.0)
    private val aggressiveModeFactor = 2.0
    private val maxThreshold = Threshold.ofPercentage(80.0)
    private val kickDownThreshold = Threshold.ofPercentage(70.0)

    private val comfortModeAdvisor = SportModeAdvisor(maxThreshold, kickDownThreshold,
            minRpm, maxRpm, aggressiveModeFactor, kickDownRpm)


    @ParameterizedTest
    @EnumSource(AggressiveMode::class)
    fun `should decrease gear if current RPM is lower than min RPM`(aggressiveMode: AggressiveMode) {
        val currentRpm = RPM.of(500.0)

        val result = comfortModeAdvisor.shouldDecreaseGear(threshold, currentRpm, aggressiveMode)

        result.shouldBeTrue()
    }

    @ParameterizedTest
    @EnumSource(AggressiveMode::class)
    fun `should not decrease gear if current threshold is greater than kick down`(aggressiveMode: AggressiveMode) {
        val currentRpm = RPM.of(2500.0)
        val currentThreshold = Threshold.ofPercentage(75.0)

        val result = comfortModeAdvisor.shouldDecreaseGear(currentThreshold, currentRpm, aggressiveMode)

        result.shouldBeFalse()
    }

    @ParameterizedTest
    @EnumSource(AggressiveMode::class)
    fun `should decrease gear if current RPM is lower than kick down RPM and threshold is suitable`(aggressiveMode: AggressiveMode) {
        val currentRpm = RPM.of(2500.0)
        val currentThreshold = Threshold.ofPercentage(65.0)

        val result = comfortModeAdvisor.shouldDecreaseGear(currentThreshold, currentRpm, aggressiveMode)

        result.shouldBeTrue()
    }

    @ParameterizedTest
    @MethodSource("aggressiveModeLevel1IncreaseGearData")
    fun `should increase gear if current RPM is greater than max RPM in aggressive mode level 1`(currentRpm: RPM) {
        val aggressiveMode = AggressiveMode.LEVEL_1

        val result = comfortModeAdvisor.shouldIncreaseGear(threshold, currentRpm, aggressiveMode)

        result.shouldBeTrue()
    }

    fun aggressiveModeLevel1IncreaseGearData(): List<RPM> = listOf(
            maxRpm + oneRPM,
            maxRpm * 1.2,
            maxRpm * aggressiveModeFactor,
            maxRpm * aggressiveModeFactor + oneRPM
    )

    @Test
    fun `should not increase gear if current threshold is high`() {
        val aggressiveMode = AggressiveMode.LEVEL_1
        val currentRpm = maxRpm + oneRPM
        val threshold = Threshold.ofPercentage(99.0)

        val result = comfortModeAdvisor.shouldIncreaseGear(threshold, currentRpm, aggressiveMode)

        result.shouldBeFalse()
    }

    @ParameterizedTest
    @MethodSource("aggressiveModeLevel1NotIncreaseGearData")
    fun `should not increase gear if current RPM is lower than max RPM in aggressive mode level 1`(currentRpm: RPM) {
        val aggressiveMode = AggressiveMode.LEVEL_1

        val result = comfortModeAdvisor.shouldIncreaseGear(threshold, currentRpm, aggressiveMode)

        result.shouldBeFalse()
    }

    fun aggressiveModeLevel1NotIncreaseGearData(): List<RPM> = listOf(
            minRpm,
            minRpm + oneRPM,
            maxRpm - oneRPM
    )

    @ParameterizedTest
    @MethodSource("aggressiveModeLevel2And3IncreaseGearData")
    fun `should increase gear if current RPM is greater than max RPM in aggressive mode level 2 and 3`(currentRpm: RPM) {

        val aggressiveModes = listOf(AggressiveMode.LEVEL_2, AggressiveMode.LEVEL_3)

        aggressiveModes.forEach {
            val result = comfortModeAdvisor.shouldIncreaseGear(threshold, currentRpm, it)

            result.shouldBeTrue()
        }
    }

    fun aggressiveModeLevel2And3IncreaseGearData(): List<RPM> = listOf(
            maxRpm * aggressiveModeFactor + oneRPM,
            maxRpm * aggressiveModeFactor * 2.0
    )

    @ParameterizedTest
    @MethodSource("aggressiveModeLevel2And3NotIncreaseGearData")
    fun `should not increase gear if current RPM is lower than max RPM in aggressive mode level 2 and 3`(currentRpm: RPM) {
        val aggressiveModes = listOf(AggressiveMode.LEVEL_2, AggressiveMode.LEVEL_3)

        aggressiveModes.forEach {
            val result = comfortModeAdvisor.shouldIncreaseGear(threshold, currentRpm, it)

            result.shouldBeFalse()
        }
    }

    fun aggressiveModeLevel2And3NotIncreaseGearData(): List<RPM> = listOf(
            minRpm,
            maxRpm + oneRPM,
            maxRpm * aggressiveModeFactor - oneRPM
    )
}