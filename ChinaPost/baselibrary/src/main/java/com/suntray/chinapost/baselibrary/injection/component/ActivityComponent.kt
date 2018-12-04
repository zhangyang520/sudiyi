package com.suntray.chinapost.baselibrary.injection.component

import android.app.Activity
import android.content.Context
import com.trello.rxlifecycle.LifecycleProvider
import dagger.Component
import com.suntray.chinapost.baselibrary.injection.ActivityScope
import com.suntray.chinapost.baselibrary.injection.module.ActivityModule
import com.suntray.chinapost.baselibrary.injection.module.LifecylerProviderModule

/**
 * Ceated by zhangyang on 2018/5/28 10:29.
 * version 1
 */
@ActivityScope
@Component(modules = arrayOf(ActivityModule::class,LifecylerProviderModule::class),dependencies = arrayOf(AppComponent::class))
interface ActivityComponent {
    fun context():Context
    fun activity():Activity
    fun lifecylerProvider(): LifecycleProvider<*>
}