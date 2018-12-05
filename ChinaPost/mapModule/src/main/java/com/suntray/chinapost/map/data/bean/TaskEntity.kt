package com.suntray.chinapost.map.data.bean

/**
 *  任务的实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/24 9:25
 */
class TaskEntity {
    var  zoneaddress="" //点位地址
    var  supplyName=""//客户名称
    var  taskArea="" //任务区域
    var  taskTime=""  //任务要求完成时间
    var  pointName=""//点位名称

    constructor(zoneaddress: String, clientName: String, taskArea: String, taskTime: String, dotName: String) {
        this.zoneaddress = zoneaddress
        this.supplyName = clientName
        this.taskArea = taskArea
        this.taskTime = taskTime
        this.pointName = dotName
    }

    constructor()

    override fun toString(): String {
        return "TaskEntity(zoneaddress='$zoneaddress', clientName='$supplyName', taskArea='$taskArea', taskTime='$taskTime', dotName='$pointName')"
    }
}