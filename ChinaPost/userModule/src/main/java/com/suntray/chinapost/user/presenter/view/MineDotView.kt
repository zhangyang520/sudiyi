package com.suntray.chinapost.user.presenter.view

import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import com.suntray.chinapost.user.data.bean.MineReservedDot

/**
 *  我预定的点位 view
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/22 18:06
 */
interface MineDotView:BaseView{

    /**
     * 获取 预定的点位列表
     */
    fun onGetMineReservedDot(reservedDotList:ArrayList<MineReservedDot>,action: RefreshAction){

    }

    /**
     * 续订点位成功
     */
    fun onReservedDot(){

    }

    fun onError(content: String, action: RefreshAction){

    }

    /**
     * \申请取消保存 的响应
     */
    fun onRelieveSaveResponse(){

    }

    fun onFindRenewDays(renewDays: Int){

    }

}