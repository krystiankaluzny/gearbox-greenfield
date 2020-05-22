package com.gearbox

class HandleGasResult(val gearChangeInfo: GearChangeInfo,
                      val soundMade: Boolean) {

    companion object {
        fun withSound(info: GearChangeInfo) = HandleGasResult(info, true)
        fun withoutSound(info: GearChangeInfo) = HandleGasResult(info, false)
    }
}

class GearChangeInfo(val changeType: ChangeType,
                     val changeValue: Int) {

    enum class ChangeType {
        INCREASE, DECREASE, NONE
    }

    companion object{
        fun increasedBy(value: Int) = GearChangeInfo(ChangeType.INCREASE, value)
        fun decreasedBy(value: Int) = GearChangeInfo(ChangeType.DECREASE, value)
        fun none() = GearChangeInfo(ChangeType.NONE, 0)
    }
}