package com.suntray.chinapost.user.ui.adapter.inner

import com.suntray.chinapost.user.data.bean.MineClient

/**
 *   点击 用户昵称的事件的回调
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/26 13:29
 */
interface ClickClientNameAction {
    /**
     * 选中对应的客户
     */
    fun  onClickClientName(mineClient: MineClient){

    }

    /**
     * 需要加载更多
     */
    fun  onNeedLoadMore(){

    }
}