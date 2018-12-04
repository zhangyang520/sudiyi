package com.suntray.chinapost.map.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.data.request.TaskListRequest
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 *  任务相关的api
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/25 9:56
 */
interface TaskApi {

    /**
     * 获取任务列表
     */
    @POST(value = BaseConstants.GET_TASK_LIST)
    fun getTaskListApi(@Body taskListRequest: TaskListRequest):Observable<BaseResp<ArrayList<TaskEntity>>>
}