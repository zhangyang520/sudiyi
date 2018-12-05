package com.suntray.chinapost.map.data.request

/**
 *   基于省市 区域查找点位
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 14:46
 */
data class ProvinceDotRequest(var province:Int,var city:Int,var district:Int,
                               var pointname:String,var isReserve:String,
                               var page:Int,var rows:Int,var salesman:Int,
                               var adtype:Int=-1,var categoryids:String="",
                               var startdate:String="",var enddate:String="")