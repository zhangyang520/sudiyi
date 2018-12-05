package com.suntray.chinapost.baselibrary.presenter

import android.content.Context
import com.trello.rxlifecycle.LifecycleProvider
import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import javax.inject.Inject

/**
 * Created by ASUS on 2018/5/25.
 */
abstract  class BasePresenter <T:BaseView>  {

    /**
     * 基础的baseView
     */
    lateinit var baseView:T

    @Inject
    lateinit var context:Context

    @Inject
    lateinit var lifecylerProvider:LifecycleProvider<*>
    /**
     * 是否要检测网络
     */
    var checkNetwork:Boolean=false;
}