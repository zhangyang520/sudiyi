package com.suntray.chinapost.map.presenter.view

import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import com.suntray.chinapost.map.data.bean.ResourceDotLocation

/**
 *   点位相关的view
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 13:34
 */
interface DotView:BaseView{

    /**
     *
     */
    fun onGetDotOfResourceList(resourceDotLocation: ResourceDotLocation){

    }
}