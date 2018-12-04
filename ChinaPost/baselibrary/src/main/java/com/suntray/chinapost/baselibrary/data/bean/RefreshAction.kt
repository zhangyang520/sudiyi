package com.suntray.chinapost.baselibrary.data.bean

/**
 *   加载对应的action
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 9:51
 */
enum class RefreshAction(var actionName:String) {
    PullDownRefresh("下拉刷新"),UpMore("上拉加载更多"),NormalAction("正常请求数据"),SearchAction("搜索数据")
}