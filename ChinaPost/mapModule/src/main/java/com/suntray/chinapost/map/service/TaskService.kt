package com.suntray.chinapost.map.service

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.data.request.TaskListRequest
import com.suntray.chinapost.map.data.request.TaskUploadRequest
import rx.Observable

/**
 *  任务的服务类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 11:39
 */
interface TaskService {

    /**
     * 获取任务列表
     */
    fun getTaskListApi(taskListRequest: TaskListRequest): Observable<ArrayList<TaskEntity>>

    /**
     * \上传任务的图片
     */
    fun uploadTaskImg(taskUpload: TaskUploadRequest):Observable<Object>
}