package com.suntray.chinapost.map.ui.adapter.proxy

import com.amap.api.services.route.BusStep

class SchemeBusStep(step: BusStep?) : BusStep() {

    var isWalk = false
    var isBus = false
    var isRailway = false
    var isTaxi = false
    var isStart = false
    var isEnd = false

    init {
        if (step != null) {
            this.busLine = step.busLine
            this.walk = step.walk
            this.railway = step.railway
            this.taxi = step.taxi
        }
    }


}
