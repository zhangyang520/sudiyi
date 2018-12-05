package com.suntray.chinapost.data.request


/**
 *   loginRequest 登录的请求实体
 *  @Author 张扬 @version 1.0
 *  @Date 20:28
 *
 *  账号类型（1管理员，4销售人员）
 */
data class LoginRequest(var email:String,var pwd:String,var type:Int)