package com.suntray.chinapost.baselibrary.injection.component

import android.content.Context
import dagger.Component
import com.suntray.chinapost.baselibrary.injection.module.AppModule

/**
 * Created by zhangyang on 2018/5/28 09:57.
 * version 1
 * 在
 */
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun context():Context
}