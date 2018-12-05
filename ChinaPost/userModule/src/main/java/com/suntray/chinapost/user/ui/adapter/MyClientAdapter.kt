package com.suntray.chinapost.user.ui.adapter

import android.app.Activity
import android.widget.ListView
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.DefaultAdapter
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.ui.holder.MyClientHolder

/**
 * 适配器
 */
class MyClientAdapter:DefaultAdapter<MineClient>{

    constructor(datas:List<MineClient>,lv:ListView,activity:Activity):super(datas,lv,activity){

    }
    override fun getHolder(): BaseHolder<MineClient> {
        return MyClientHolder()
    }

    override fun processDatasList() {

    }
}