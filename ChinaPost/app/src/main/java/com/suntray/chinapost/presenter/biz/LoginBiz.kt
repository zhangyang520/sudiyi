package com.suntray.chinapost.presenter.biz

import com.google.gson.Gson
import com.suntray.chinapost.baselibrary.data.bean.User


/**
 *  登录业务的解析类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 21:18
 */
object LoginBiz {

    /**
     * 解析登录
     */
    fun parseLogin(jsonString:String):User{
       return User()
    }
}