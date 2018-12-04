package com.suntray.chinapost.data.respository

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.bean.User
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.data.api.LoginApi
import com.suntray.chinapost.data.request.LoginRequest
import rx.Observable
import javax.inject.Inject

/**
 *  用户登录的 对应的仓库
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 20:30
 */
class UserLoginRespository @Inject constructor() {

    /**
     * 登录的仓库
     */
    fun login(loginRequest: LoginRequest):Observable<BaseResp<User>>{
        return RetrofitFactory.instance.create(LoginApi::class.java).loginReq(loginRequest);
    }
}