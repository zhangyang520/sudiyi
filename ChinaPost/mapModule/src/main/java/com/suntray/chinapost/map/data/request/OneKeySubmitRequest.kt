package com.suntray.chinapost.map.data.request

/**
 *   一键预定的封装
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 18:55
 */
data class OneKeySubmitRequest(
    var id: Array<Int?>? = null ,//点位id数组
    var resourceid: Array<Int?>? = null, //资源位类别字典id数组（包含柜屏和柜贴）
    var clientid:Int=0 ,//客户ID
    var adtype:Int=0 ,//广告类型字典id
    var userId:Int=0 ,//当前登录用户ID
    var startdate:String="" ,//预约开始时间（2018/09/09）
    var enddate:String="", //预约开始时间（2018/09/09)
    var clientName:String ="",  //客户名称
    var userType:Int =-1 // 2代理商，3供应商，4销售人员
)