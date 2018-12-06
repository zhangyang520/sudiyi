package com.suntray.chinapost.user.data.bean

import java.io.Serializable

/**
 *  task的上传实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/24 13:47
 */
class TaskUpload:Serializable{
    var id:Int=0 //id
    var address="" //资质的上传图片的地址
    var accessoryid="" //
    var name="" //图片的名称
    var size="" //图片的大小 字符串类型

    var tasktype=0
    var imgpathid=""
    var taskid=0
    var state=0
    var taskpointid=0
    var imgPath=""

    constructor()
    constructor(address: String) {
        this.address = address
        this.imgPath = address
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskUpload

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "TaskUpload(id=$id, address='$address', accessoryid='$accessoryid', name='$name', size='$size', tasktype=$tasktype, " +
                "imgpathid='$imgpathid', taskid=$taskid, state=$state, taskpointid=$taskpointid, imgPath='$imgPath')"
    }


}