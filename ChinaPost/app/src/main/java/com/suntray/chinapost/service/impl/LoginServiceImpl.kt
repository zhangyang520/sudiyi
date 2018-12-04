package com.suntray.chinapost.service.impl

import com.suntray.chinapost.baselibrary.data.bean.User
import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.data.request.LoginRequest
import com.suntray.chinapost.data.respository.UserLoginRespository
import com.suntray.chinapost.service.LoginService
import rx.Observable
import javax.inject.Inject

/**
 *   登录服务的实现类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 20:51
 */
class LoginServiceImpl @Inject constructor():LoginService{

    @Inject
    protected lateinit var loginRespository: UserLoginRespository


    override fun loginRequest(loginRequest: LoginRequest): Observable<User> {
        return loginRespository.login(loginRequest).convertData()
    }
}