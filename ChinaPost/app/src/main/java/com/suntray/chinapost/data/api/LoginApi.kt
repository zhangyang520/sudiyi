package com.suntray.chinapost.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.bean.User
import com.suntray.chinapost.data.request.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable


/**
 *  登录的api
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 19:46
 */
interface LoginApi {


   @POST(BaseConstants.LOGIN)
   fun loginReq(@Body loginRequest: LoginRequest):Observable<BaseResp<User>>
}