package com.suntray.chinapost.map.injection.component

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.baselibrary.injection.component.ActivityComponent
import com.suntray.chinapost.map.injection.module.TaskModule
import com.suntray.chinapost.map.ui.activity.proxy.TaskDetailActivity
import com.suntray.chinapost.map.ui.activity.proxy.TaskListActivity
import dagger.Component

/**
 *   任务的component
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 11:41
 */
@PerComponentScope
@Component(modules = arrayOf(TaskModule::class),dependencies = arrayOf(ActivityComponent::class))
interface TaskComponent {
    fun bind(taskListActivity: TaskListActivity)

    fun bind(taskDetailActivity: TaskDetailActivity)
}