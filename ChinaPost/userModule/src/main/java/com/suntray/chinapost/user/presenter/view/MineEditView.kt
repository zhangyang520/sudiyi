package com.suntray.chinapost.user.presenter.view

import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineMessage

/**
 *  我的界面的回调
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:52
 */
interface MineEditView:BaseView{

    /**
     * 修改密码的回调
     */
    fun onEditPwdRequest(){

    }

    /**
     * 上传头像成功
     */
    fun onUploadPortrait(content:String){

    }

    /**
     * 我的客户数据的回调
     */
    fun onMineClient(myClientList:ArrayList<MineClient>,action:RefreshAction){


    }

    /**
     * 我的客户数据的回调
     */
    fun onMineMessage(myClientList:ArrayList<MineMessage>,action:RefreshAction){


    }

    /**
     * 错误的请求
     */
    fun  onError(content:String,action:RefreshAction){

    }
}