package com.suntray.chinapost.map.data.bean

import java.io.Serializable

/**
 *  任务的图片的实体
 */
class TaskImage:Serializable{
    var tasktype=0
    var imgpathid=""
    var taskid=0
    var state=0
    var taskpointid=0
    var imgPath=""

    constructor()
    constructor(tasktype: Int, imgpathid: String, taskid: Int, state: Int, taskpointid: Int, imgPath: String) {
        this.tasktype = tasktype
        this.imgpathid = imgpathid
        this.taskid = taskid
        this.state = state
        this.taskpointid = taskpointid
        this.imgPath = imgPath
    }

    override fun toString(): String {
        return "TaskImage(tasktype=$tasktype, imgpathid='$imgpathid', taskid=$taskid, state=$state, taskpointid=$taskpointid, imgPath='$imgPath')"
    }


}