package com.suntray.chinapost.inject.component

import com.suntray.chinapost.LoginActivity
import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.baselibrary.injection.component.ActivityComponent
import com.suntray.chinapost.inject.module.LoginModule
import dagger.Component

/**
 *   登录的component
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 21:12
 */
@Component(modules = arrayOf(LoginModule::class),dependencies = arrayOf(ActivityComponent::class))
@PerComponentScope
interface LoginComponent {
    fun bind(loginActivity: LoginActivity)
}