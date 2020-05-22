package com.gearbox

import com.gearbox.drivemodeadvisor.DriveModeAdvisor
import com.gearbox.drivemodeadvisor.DriveModeAdvisorFactory
import com.gearbox.rpm.RPM
import com.gearbox.rpm.RpmService
import com.gearbox.sound.SoundModule
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class GearboxDriverTest {

    private val gearboxAdapter = mockk<GearboxAdapter>(relaxUnitFun = true)
    private val rpmService = mockk<RpmService>()
    private val advisor = mockk<DriveModeAdvisor>()
    private val driveModeAdvisorFactory = object : DriveModeAdvisorFactory {
        override fun getAdvisor(driveMode: DriveMode) = advisor
    }
    private val soundModule = SoundModule()

    private val gearboxDriver = GearboxDriver(gearboxAdapter, rpmService, driveModeAdvisorFactory, soundModule)



    @ParameterizedTest
    @EnumSource(value = GearboxState::class, names = ["DRIVE"], mode = EnumSource.Mode.EXCLUDE)
    fun `should do nothing if no drive mode`(gearboxState: GearboxState) {

        every { gearboxAdapter.getState() } returns gearboxState

        val result = gearboxDriver.handleGas(Threshold.ofPercentage(50.0))

        result.gearChangeInfo.changeType shouldBe GearChangeInfo.ChangeType.NONE
        result.gearChangeInfo.changeValue shouldBe 0
        result.soundMade.shouldBeFalse()
    }

    @Test
    fun `should increase gear if advisor say that`() {

        //given
        every { gearboxAdapter.getState() } returns GearboxState.DRIVE
        every { gearboxAdapter.getCurrentGear() } returns 2
        every { gearboxAdapter.canIncreaseGear() } returns true
        every { rpmService.getCurrentRpm() } returns RPM.of(20.0)
        every { advisor.shouldIncreaseGear(allAny(), allAny(), allAny()) } returns true
        every { advisor.shouldDecreaseGear(allAny(), allAny(), allAny()) } returns false

        //when
        val result = gearboxDriver.handleGas(Threshold.ofPercentage(50.0))

        //then
        result.gearChangeInfo.changeType shouldBe GearChangeInfo.ChangeType.INCREASE
        result.gearChangeInfo.changeValue shouldBe 1

        verify(exactly = 1) { gearboxAdapter.increaseGear() }
    }

    @Test
    fun `should decrease gear if advisor say that`() {

        //given
        every { gearboxAdapter.getState() } returns GearboxState.DRIVE
        every { gearboxAdapter.getCurrentGear() } returns 2
        every { gearboxAdapter.canDecreaseGear() } returns true
        every { rpmService.getCurrentRpm() } returns RPM.of(20.0)
        every { advisor.shouldIncreaseGear(allAny(), allAny(), allAny()) } returns false
        every { advisor.shouldDecreaseGear(allAny(), allAny(), allAny()) } returns true

        //when
        val result = gearboxDriver.handleGas(Threshold.ofPercentage(50.0))

        //then
        result.gearChangeInfo.changeType shouldBe GearChangeInfo.ChangeType.DECREASE
        result.gearChangeInfo.changeValue shouldBe 1

        verify(exactly = 1) { gearboxAdapter.decreaseGear() }
    }
}