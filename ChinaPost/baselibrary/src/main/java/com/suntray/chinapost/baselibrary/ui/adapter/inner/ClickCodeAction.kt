package com.suntray.chinapost.baselibrary.ui.adapter.inner

import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity

/**
 *  点击 城市时 回调
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 20:32
 */
interface ClickCodeAction {
    /**
     * 点击条目的时候的回调
     */
    fun clickItem(code:Int, action: CityListAction, provinceCity: ProvinceCity){

    }

    /**
     * 点击 区域的条目时候  业务回调
     */
    fun clickDistrict(provinceCity: ProvinceCity){

    }
}