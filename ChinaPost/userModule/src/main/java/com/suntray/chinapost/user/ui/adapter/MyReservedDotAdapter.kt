package com.suntray.chinapost.user.ui.adapter

import android.app.Activity
import android.widget.ListView
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.DefaultAdapter
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.presenter.MineDotPresenter
import com.suntray.chinapost.user.ui.activity.MineReservedDotActivity
import com.suntray.chinapost.user.ui.holder.MyReservedDotHolder

/**
 *   我预定的点位适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/27 0:13
 */
class MyReservedDotAdapter: DefaultAdapter<MineReservedDot> {
    var selectPositionList:ArrayList<Int> = arrayListOf()
    constructor(datas: MutableList<MineReservedDot>?,
                lv: ListView?, activity: MineReservedDotActivity?,
                basePresenter: MineDotPresenter):super(datas,lv,activity){
        this.basePresenter=basePresenter
    }
    var basePresenter:MineDotPresenter?=null
    var myReservedDotHolder:MyReservedDotHolder?=null
    override fun getHolder(): BaseHolder<MineReservedDot> {
        myReservedDotHolder=MyReservedDotHolder(basePresenter!!,activity as MineReservedDotActivity,selectPositionList)
        return myReservedDotHolder!!
    }

    override fun processDatasList() {

    }


    /**
     * 处理显示 所有的显示数据
     */
    fun  processAllSelect(){
        MyReservedDotHolder!!.isNeedShowSelected=true
        myReservedDotHolder!!.selectPositionList!!.clear()
        notifyDataSetChanged()
    }
    /**
     * 处理取消全选
     */
    fun processAntiAllSelect(){
        MyReservedDotHolder!!.isNeedShowSelected=false
        myReservedDotHolder!!.selectPositionList!!.clear()
        notifyDataSetChanged()
    }

    fun dismissDilog() {
        MyReservedDotHolder!!.dismissDilog()
    }
}