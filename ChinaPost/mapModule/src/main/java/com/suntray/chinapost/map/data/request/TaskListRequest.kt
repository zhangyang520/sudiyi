package com.suntray.chinapost.map.data.request

/**
 *   task相关任务列表的请求数据
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/25 9:57
 */
data class TaskListRequest(var userId:Int,var name:String,var page:Int,var rows:Int,var type:Int,var state:Int,var supplyID:String="")