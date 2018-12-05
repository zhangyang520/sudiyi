package com.suntray.chinapost.user.data.request

/**
 *  我的客户的请求
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 16:37
 */
data class MyClientRequest(var salesman:Int,var name:String,var page:Int,var rows:Int,var stage:Int=-1,var searchtype:Int)