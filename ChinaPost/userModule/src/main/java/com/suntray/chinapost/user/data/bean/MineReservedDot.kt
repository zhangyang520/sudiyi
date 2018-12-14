package com.suntray.chinapost.user.data.bean

/**
 *   我预定的点位
 *   @Author 张扬 @version 1.0
 *   @Date 2018/9/21 22:49
 */
class MineReservedDot {
    var reserveman:Int?=0 //
    var createby:Int?=0 //
    var pointid:Int?=0 //
    var createdate:String=""//
    var state:Int=0//
    var statename="" //阶段名称
    var adtype:Int=0//
    var clientname:String=""
    var id:Int=0
    var salesman:Int=0//
    var clientid:Int=0
    var adtypename:String=""
    var resourcetypename:String=""
    var startdate:String=""
    var enddate:String=""
    var validenddate:String=""

    override fun toString(): String {
        return "MineReservedDot(reserveman=$reserveman, createby=$createby, pointid=$pointid, createdate='$createdate', state=$state, adtype=$adtype, clientname='$clientname', id=$id, salesman=$salesman, clientid=$clientid, adtypename='$adtypename', resourcetypename='$resourcetypename', startdate='$startdate', enddate='$enddate')"
    }


}