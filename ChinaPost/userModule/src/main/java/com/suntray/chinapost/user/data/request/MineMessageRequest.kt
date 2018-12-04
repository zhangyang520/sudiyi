package com.suntray.chinapost.user.data.request

/**
 * 消息的请求实体
 */
data class MineMessageRequest(var receiver	:Int,var page:Int=0,var rows:Int=10)