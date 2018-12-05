package com.suntray.chinapost.user.data.bean

/**
 *  修改密码的请求数据
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:35
 */
data class EditPwdRequest(var email:String,var pwd:String,var newPwd:String,var id:Int,var type:Int)