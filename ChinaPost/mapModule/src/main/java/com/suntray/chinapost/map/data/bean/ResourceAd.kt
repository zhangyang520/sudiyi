package com.suntray.chinapost.map.data.bean

import java.io.Serializable

/**
 *  资源广告
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 13:11
 */
class ResourceAd:Serializable{
    var pointid:Int=0//点位的id
    var expiredate:String=""//过期时间
    var publishdate:String="" //发布时间
    var salesmanname=""//销售人员名称
    var startDate=""//开始时间
    var endDate=""//结束时间
    var type:Int=0 //类型
    var id:Int=0// 资源位的id
    var category:Int=0//
    var name:String=""//资源位的名称
    var state:Int=0 //对应的资源位的状态
    var statename=""
    var typename=""//类型名称
    var taskType=-1 //默认 没有上 下刊报告
    var uploadDate="" //报告完成日期
    
    override fun toString(): String {
        return "ResourceAd(pointid=$pointid, expiredate='$expiredate', " +
                   "publishdate='$publishdate', salesmanname='$salesmanname', " +
                         "startDate='$startDate', endDate='$endDate', type=$type, id=$id, " +
                                                        "category=$category, name='$name', state=$state)"
    }
}