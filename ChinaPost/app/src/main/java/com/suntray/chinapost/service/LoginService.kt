package com.suntray.chinapost.service

import com.suntray.chinapost.baselibrary.data.bean.User
import com.suntray.chinapost.data.request.LoginRequest
import rx.Observable

/**
 *   登录的相关的服务接口
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 20:48
 */
interface LoginService {

    /**
     * 登录的接口调用
     */
    fun loginRequest(loginRequest: LoginRequest):Observable<User>
}