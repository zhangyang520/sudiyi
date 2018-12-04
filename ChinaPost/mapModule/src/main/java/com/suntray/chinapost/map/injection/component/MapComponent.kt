package com.suntray.chinapost.map.injection.component

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.baselibrary.injection.component.ActivityComponent
import com.suntray.chinapost.map.injection.module.MapModule
import com.suntray.chinapost.map.ui.activity.sale.PostAdOneKeyReservedActivity
import com.suntray.chinapost.map.ui.activity.sale.PostPoiSearchActivity
import dagger.Component

/**
 *   地图的component
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 15:41
 */
@PerComponentScope
@Component(modules = arrayOf(MapModule::class),dependencies = arrayOf(ActivityComponent::class))
interface MapComponent {

    /**
     * 绑定该界面
     */
    fun bind(poiSearchActivity: PostPoiSearchActivity)

    /**
     * 绑定 一键提交界面
     */
    fun bind(postAdOneKeyReservedActivity: PostAdOneKeyReservedActivity)
}