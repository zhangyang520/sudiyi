package com.suntray.chinapost.map.data.request

/**
 * 消息 通知数量 的请求
 */
data class NewNoticeCountRequest(
     var receiver:Int=0,
     var receivertype:Int=0
)