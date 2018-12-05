package com.suntray.chinapost.user.ui.adapter

import android.app.Activity
import android.widget.ListView
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.DefaultAdapter
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.ui.activity.MineClientActivity
import com.suntray.chinapost.user.ui.holder.MyClientHolder
import com.suntray.chinapost.user.ui.holder.MyMessageHolder

/**
 * 适配器
 */
class MyMessageAdapter:DefaultAdapter<MineMessage>{

    constructor(datas:List<MineMessage>,lv:ListView,activity: Activity):super(datas,lv,activity){
    }
    override fun getHolder(): BaseHolder<MineMessage> {
        return MyMessageHolder()
    }

    override fun processDatasList() {

    }
}