package com.suntray.chinapost.presenter

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.User
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.data.request.LoginRequest
import com.suntray.chinapost.presenter.view.LoginView
import com.suntray.chinapost.service.impl.LoginServiceImpl
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 *   登录相关的presenter
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 21:14
 */
class LoginPresenter @Inject constructor():BasePresenter<LoginView>(){

    @Inject
    lateinit var loginService: LoginServiceImpl

    /**
     * 登录 逻辑
     */
    fun loginBiz(userName:String,userPwd:String,selectRoleIndex:Int){
        //销售人员
        loginService.loginRequest(LoginRequest(userName, userPwd, selectRoleIndex)).execute(
                object :BaseSucriber<User>(baseView,LoginPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        e!!.printStackTrace()
                        var errorMsg="";
                        if(e is SocketTimeoutException){
                            errorMsg="登录操作,请求超时";
                        }else if(e is ContentException){
                            errorMsg=e.errorContent;
                        }else{
                            errorMsg="登录操作,请求失败";
                        }
                        assertMethod(baseView,{
                            (baseView as LoginView).onError(errorMsg, BaseConstants.LOGIN);
                        })
                    }
                    override fun onNext(t: User) {
                        super.onNext(t)
                        SystemUtil.printlnStr("loginBiz t:"+t.toString())
                        t.pwd=userPwd;
                        try {
                            var user= UserDao.getUserId(t.id.toString())
                            user.userRole=selectRoleIndex
                            UserDao.deleteUser(user)
                            t.isLocalUser=true
                            UserDao.saveUpDate(t)
                        } catch (e: ContentException) {
                            //没有该用户进行保存
                            t.isLocalUser=true
                            t.userRole=selectRoleIndex
                            UserDao.saveUpDate(t)
                        }
                        assertMethod(baseView,{
                            (baseView as LoginView).onLogignRequest();
                        })
                    }
        },lifecylerProvider);
    }
}