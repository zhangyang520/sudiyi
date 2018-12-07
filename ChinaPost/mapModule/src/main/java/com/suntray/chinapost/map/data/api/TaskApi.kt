package com.suntray.chinapost.map.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.data.bean.TaskItem
import com.suntray.chinapost.map.data.request.TaskListRequest
import okhttp3.MultipartBody
import retrofit2.http.*
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

    /**
     * 获取任务列表 第二个接口
     */
    @POST(value = BaseConstants.GET_TASK_LIST_2)
    fun getTaskListApi2(@Body taskListRequest: TaskListRequest):Observable<BaseResp<TaskItem>>

    /**
     * pointTaskId	是	string	点位任务id
      taskId	是	string	总任务id
      type	是	int	上下刊类型：1上刊 2下刊
      userId	是	int	用户id
      imgFiles	是	List<MultipartFile>	图片
      imgIds	是	string[]	新增或修改图片id，顺序与imgFiles一致 ，新增时对应值为空
      deleteIds	是	string[]	删除图片id
     */
    @Multipart
    @POST(value = BaseConstants.UPLOAT_TASK_IMG)
    fun uploadTaskImg(@Query(value = "pointTaskId")pointTaskId:String,
                      @Query(value = "taskId")taskId:String,
                      @Query(value = "type")type:Int,
                      @Query(value = "userId")userId:Int,
                      @Part imgFiles: List<MultipartBody.Part>,
                      @Query(value = "imgIds")imgIds:Array<Int?>,
                      @Query(value = "deleteIds")deleteIds:Array<Int?>):Observable<BaseResp<Object>>
}