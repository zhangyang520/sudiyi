package com.suntray.chinapost.map.ui.adapter.proxy

import android.app.Activity
import android.widget.ListView
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.DefaultAdapter
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.ui.holder.TaskHolder

/**
 *  任务ListView的适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/25 10:30
 */
class TaskListViewAdapter: DefaultAdapter<TaskEntity> {

    var currentType:Int=0
    var firstType:Int=0
    constructor(datas:List<TaskEntity>, lv: ListView, activity: Activity,currentType:Int,firstType:Int):super(datas,lv,activity){
        this.currentType=currentType
        this.firstType=firstType
    }
    override fun getHolder(): BaseHolder<TaskEntity> {
        return TaskHolder(currentType,firstType,this)
    }

    override fun processDatasList() {

    }
}