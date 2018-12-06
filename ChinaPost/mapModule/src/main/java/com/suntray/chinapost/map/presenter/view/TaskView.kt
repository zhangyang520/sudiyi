package com.suntray.chinapost.map.presenter.view

import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import com.suntray.chinapost.map.data.bean.TaskEntity

/**
 *  任务界面的view
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 11:36
 */
interface TaskView: BaseView {
    /**
     * 获取 未完成的任务列表
     */
    fun onGetUnfinishedList(taskList:ArrayList<TaskEntity>,action:RefreshAction){

    }

    /**
     * 获取 待审核的任务列表
     */
    fun  onGetWillExamineList(taskList:ArrayList<TaskEntity>,action:RefreshAction){

    }

    /**
     * 获取 审核不通过的任务列表
     */
    fun  onGetNotExamineList(taskList:ArrayList<TaskEntity>,action:RefreshAction){

    }

    /**
     * 获取 审核通过的任务列表
     */
    fun  onGetExamineList(taskList:ArrayList<TaskEntity>,action:RefreshAction){

    }

    /**
     * 错误的请求
     */
    fun  onError(content:String,action:RefreshAction,position:Int){

    }

    /**
     * 错误的请求
     */
    fun  onError(content:String,action:RefreshAction){

    }

    /**
     * 上传 任务的图片
     */
    fun  onUploadTaskImg(){}
}