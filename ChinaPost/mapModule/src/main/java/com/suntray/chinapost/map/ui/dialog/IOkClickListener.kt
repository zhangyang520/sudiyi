package com.suntray.chinapost.map.ui.dialog

/**
 *  点击事件
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/19 16:04
 */
interface IOkClickListener {
    /**
     * 点击点位名称的函数
     */
    fun  onDotNameClickListener()

    /**
     * 点击地理位置的回调函数
     */
    fun  onPositionClickListener()
}