package com.suntray.chinapost.map.data.bean

/**
 *  任务列表的 条目实体
 *  @Title ${name}
 *  @ProjectName ChinaPost
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/718:25
 */
class TaskItem {
    constructor(count: Int, list: ArrayList<TaskEntity>?) {
        this.count = count
        this.list = list
    }
    constructor()

    var count=0
    var list:ArrayList<TaskEntity>?=null


    override fun toString(): String {
        return "TaskItem(count=$count, list=$list)"
    }
}