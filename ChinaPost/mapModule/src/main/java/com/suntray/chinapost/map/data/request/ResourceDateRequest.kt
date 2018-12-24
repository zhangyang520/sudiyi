package com.suntray.chinapost.map.data.request

/**
 *   资源周期的请求体的封装
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/27 17:32
 */
data class ResourceDateRequest(var id:Int,var chooseDate:String,var userId:Int=0,var userType:Int=0)