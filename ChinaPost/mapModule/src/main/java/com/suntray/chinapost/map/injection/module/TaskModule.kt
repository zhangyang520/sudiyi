package com.suntray.chinapost.map.injection.module

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.map.service.TaskService
import com.suntray.chinapost.map.service.impl.TaskServiceImpl
import dagger.Module
import dagger.Provides

/**
 *   任务界面的 module类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 11:38
 */
@Module
@PerComponentScope
class TaskModule {

    @Provides
    fun provideTaskService(taskService: TaskServiceImpl): TaskService {
        return taskService
    }
}