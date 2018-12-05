package com.suntray.chinapost.map.injection.component

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.baselibrary.injection.component.ActivityComponent
import com.suntray.chinapost.map.injection.module.ResourceModule
import com.suntray.chinapost.map.ui.activity.sale.PostAdInfoActivity
import com.suntray.chinapost.map.ui.activity.sale.PostAdResorceReservedActivity
import com.suntray.chinapost.map.ui.activity.sale.PostReservedAdResultActivity
import dagger.Component


/**
 *  资源位 的 component
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 13:26
 */
@PerComponentScope
@Component(modules = arrayOf(ResourceModule::class),dependencies = arrayOf(ActivityComponent::class))
interface ResourceComponent {

    /**
     * 绑定 点位的 资源位列表信息
     */
    fun bind(rdInfoActivity: PostAdInfoActivity)

    /***
     * 绑定广告资源位预定
     */
    fun bind(postAdResorceReservedActivity: PostAdResorceReservedActivity)

    /**
     * 绑定结果界面
     */
    fun bind(postReservedAdResultActivity: PostReservedAdResultActivity)
}