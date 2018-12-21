package com.suntray.chinapost.map.data.request

/**
 *   半径点位的 实体封装
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 14:43
 */
data class RadiusDotRequest(var longitude:Double,var latitude:Double,
                             var radius:Double,var pointname:String,
                             var isReserve:String,var page:Int,
                             var rows:Int,var salesman:Int,
                             var city:Int=-1,var district:Int=-1,
                             var searchtype:Int=1,var adtype:Int=-1,
                             var categoryids:String="",var startdate:String="",
                             var enddate:String="",var salesmanType:Int)