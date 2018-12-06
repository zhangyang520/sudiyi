package com.suntray.chinapost.map.data.bean

import com.suntray.chinapost.user.data.bean.TaskUpload
import java.io.Serializable

/**
 *  任务的实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/24 9:25
 */
class TaskEntity:Serializable {
    var  zoneaddress="" //点位地址
    var  supplyName=""//客户名称
    var  taskArea="" //任务区域
    var  taskTime=""  //任务要求完成时间
    var  pointName=""//点位名称
    var  imgs:ArrayList<TaskUpload>?=null
    var taskType="" //上刊类型
    var zoneType="" //小区类型
    var equId="" //设备的id
    var publishType="" //限制上刊类型
    var equSpecify="" //设备的规格
    var adverType="" //广告类型
    var state="" //任务的状态
    var taskId="" //任务的id
    var pointTaskId="" //点位任务的id

    constructor(zoneaddress: String, clientName: String, taskArea: String, taskTime: String, dotName: String) {
        this.zoneaddress = zoneaddress
        this.supplyName = clientName
        this.taskArea = taskArea
        this.taskTime = taskTime
        this.pointName = dotName
    }

    constructor()

    override fun toString(): String {
        return "TaskEntity(zoneaddress='$zoneaddress', supplyName='$supplyName', taskArea='$taskArea', taskTime='$taskTime', pointName='$pointName', taskImageList=$imgs)"
    }


}