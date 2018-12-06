package com.suntray.chinapost.map.service.impl

import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.data.request.TaskListRequest
import com.suntray.chinapost.map.data.request.TaskUploadRequest
import com.suntray.chinapost.map.data.respository.TaskRespository
import com.suntray.chinapost.map.service.TaskService
import rx.Observable
import javax.inject.Inject

/**
 *  任务的实现类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 11:40
 */
class TaskServiceImpl @Inject constructor():TaskService{

    @Inject
    lateinit var taskRespository: TaskRespository

    /**
     * 获取任务列表
     */
    override fun getTaskListApi(taskListRequest: TaskListRequest): Observable<ArrayList<TaskEntity>> {
        return taskRespository.getTaskListApi(taskListRequest).convertData()
    }

    /**
     * 上传任务的图片
     */
    override fun uploadTaskImg(taskUpload: TaskUploadRequest): Observable<Object> {
        return taskRespository.uploadTaskImg(taskUpload).convertData()
    }
}