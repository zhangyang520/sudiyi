package com.suntray.chinapost.user.ui.adapter

import android.app.Activity
import android.widget.ListView
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.DefaultAdapter
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.presenter.MineDotPresenter
import com.suntray.chinapost.user.ui.holder.MyReservedDotHolder

/**
 *   我预定的点位适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/27 0:13
 */
class MyReservedDotAdapter: DefaultAdapter<MineReservedDot> {

    constructor(datas: MutableList<MineReservedDot>?,
                lv: ListView?, activity: Activity?,
                basePresenter: MineDotPresenter):super(datas,lv,activity){
        this.basePresenter=basePresenter
    }
    var basePresenter:MineDotPresenter?=null

    var myReservedDotHolder:MyReservedDotHolder?=null
    override fun getHolder(): BaseHolder<MineReservedDot> {
        myReservedDotHolder=MyReservedDotHolder(basePresenter!!)
        return myReservedDotHolder!!
    }

    override fun processDatasList() {

    }

    fun dismissDilog() {
        MyReservedDotHolder!!.dismissDilog()
    }
}