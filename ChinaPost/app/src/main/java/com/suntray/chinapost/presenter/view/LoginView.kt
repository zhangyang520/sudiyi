package com.suntray.chinapost.presenter.view

import com.suntray.chinapost.baselibrary.data.bean.User
import com.suntray.chinapost.baselibrary.presenter.view.BaseView

/**
 *   登录相关的view
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 21:15
 */
interface LoginView:BaseView{

    /**
     *  登录成功返回
     */
    fun onLogignRequest(t: User);
}