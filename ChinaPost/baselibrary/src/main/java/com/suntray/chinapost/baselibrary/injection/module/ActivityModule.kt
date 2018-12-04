package com.suntray.chinapost.baselibrary.injection.module

import android.app.Activity
import dagger.Module
import dagger.Provides
import com.suntray.chinapost.baselibrary.injection.ActivityScope

/**
 * Created by zhangyang on 2018/5/28 10:27.
 * version 1
 */
@ActivityScope
@Module
class ActivityModule(private val activity:Activity) {

    @Provides
    fun provideActivity():Activity{
        return activity
    }
}