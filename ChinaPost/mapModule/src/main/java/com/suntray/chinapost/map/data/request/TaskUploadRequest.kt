package com.suntray.chinapost.map.data.request

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart

/**
 *  任务上传的 请求体
 *  @Title ${name}
 *  @ProjectName ChinaPost
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/620:10
 */
class TaskUploadRequest {
    var pointTaskId="" //点位任务id
    var taskId=""// 	总任务id
    var type=1 // 上下刊类型：1上刊 2下刊
    var userId=-1 //  用户id
    var imgFiles:List<MultipartBody.Part> ?=null // List<MultipartFile>	图片
    var imgIds:Array<Int?> ? =null // 新增或修改图片id，顺序与imgFiles一致 ，新增时对应值为空
    var deleteIds:Array<Int?> ? =null // 删除图片id
    var descritpion: RequestBody?=null
    constructor(pointTaskId: String, taskId: String, type: Int, userId: Int, imgFiles: List<MultipartBody.Part>?, imgIds: Array<Int?>?, deleteIds: Array<Int?>?,descritpion: RequestBody?) {
        this.pointTaskId = pointTaskId
        this.taskId = taskId
        this.type = type
        this.userId = userId
        this.imgFiles = imgFiles
        this.imgIds = imgIds
        this.deleteIds = deleteIds
        this.descritpion=descritpion
    }
}