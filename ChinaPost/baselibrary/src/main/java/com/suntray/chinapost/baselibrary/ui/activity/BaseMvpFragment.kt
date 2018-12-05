package com.suntray.chinapost.baselibrary.ui.activity

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.suntray.chinapost.baselibrary.R
import com.suntray.chinapost.baselibrary.common.BaseApplication
import com.suntray.chinapost.baselibrary.injection.component.ActivityComponent
import com.suntray.chinapost.baselibrary.injection.component.DaggerActivityComponent
import com.suntray.chinapost.baselibrary.injection.module.ActivityModule
import com.suntray.chinapost.baselibrary.injection.module.LifecylerProviderModule
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import javax.inject.Inject


/**
 *  Created by ASUS on 2018/5/25.
 *  基础的mvp框架的activity类
 *
 */
 abstract class BaseMvpFragment<T:BasePresenter<*>>:BaseFragmenAcvitiy(), BaseView{

    protected lateinit  var activityComponent:ActivityComponent;

    var hud2:KProgressHUD?=null
    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        initAcitivityInjection();
        injectCompontent()
        super.onCreate(savedInstanceState)
    }

    private fun initAcitivityInjection() {
        activityComponent=DaggerActivityComponent.builder().
                                            activityModule(ActivityModule(this)).
                                            lifecylerProviderModule(LifecylerProviderModule(this)).
                                            appComponent((applicationContext as BaseApplication).appComponent).build();
    }

    /**
     * 注入component
     */
    abstract fun injectCompontent()

    /**
     * 基础的basePresenter的类
     */
    @Inject
    lateinit var  basePresenter:T


    /**
     * 展示进度条
     */
    override fun showLoading(){
        if(hud2!=null && !hud2!!.isShowing){
            hud2?.show()
        }
   }

    /**
     * 隐藏进度条
     */
    override fun hideLoading(){
        if(hud2!=null && hud2!!.isShowing){
            hud2?.dismiss()
        }
    }

    /**
     * 错误的反馈
     */
    override fun onError(error:String,vararg args:String){
        ToastUtil.makeText(this,error);
    }

    /**
     * 登录失败的反馈
     */
    override fun onLoginError(error: String, code: String) {
        outLoginState(this,error);
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * 需要置为空
         */
//        this.basePresenter.baseView
//        this.basePresenter!=null
    }
}