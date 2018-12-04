package com.suntray.chinapost.inject.module

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.service.LoginService
import com.suntray.chinapost.service.impl.LoginServiceImpl
import dagger.Module
import dagger.Provides

/**
 *   login相关的module
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 20:54
 */
@PerComponentScope
@Module
class LoginModule {

    @Provides
    fun providesLoginService(loginService: LoginServiceImpl):LoginService{
        return loginService;
    }
}