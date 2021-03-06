package com.suntray.chinapost.map.data.respository

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.map.data.api.TaskApi
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.data.bean.TaskItem
import com.suntray.chinapost.map.data.request.TaskListRequest
import com.suntray.chinapost.map.data.request.TaskNumberRequest
import com.suntray.chinapost.map.data.request.TaskUploadRequest
import com.suntray.chinapost.map.data.response.TaskNumberResponse
import retrofit2.http.Body
import rx.Observable
import javax.inject.Inject

/**
 *   任务相关业务的 网络仓库
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/25 10:43
 */
class TaskRespository @Inject constructor(){

    /**
     * 访问任务列表
     */
    fun getTaskListApi(taskListRequest: TaskListRequest): Observable<BaseResp<ArrayList<TaskEntity>>>{
        return RetrofitFactory.instance.create(TaskApi::class.java).getTaskListApi(taskListRequest)
    }

    /**
     * 获取任务列表 第二个接口
     */
    fun getTaskListApi2(taskListRequest: TaskListRequest):Observable<BaseResp<TaskItem>>{
        return RetrofitFactory.instance.create(TaskApi::class.java).getTaskListApi2(taskListRequest)
    }
  /**
　　* @Description: 上传任务的图片
　　* @param
　　* @return
　　* @throws
　　* @author zhangyang
　　* @date 2018/12/6 20:25
　　*/
    fun uploadTaskImg(taskUpload:TaskUploadRequest):Observable<BaseResp<Object>>{
        return RetrofitFactory.instance.create(TaskApi::class.java).
                            uploadTaskImg(taskUpload.pointTaskId,taskUpload.taskId,taskUpload.type,
                                            taskUpload.userId,taskUpload.imgFiles!!,taskUpload.imgIds!!,taskUpload.deleteIds!!,taskUpload!!.descritpion!!)
    }


    /**
     * 获取任务数量
     */
    fun getTaskNumber(taskNumberRequest: TaskNumberRequest):Observable<BaseResp<TaskNumberResponse>>{
        return RetrofitFactory.instance.create(TaskApi::class.java).getTaskNumber(taskNumberRequest)
    }
}