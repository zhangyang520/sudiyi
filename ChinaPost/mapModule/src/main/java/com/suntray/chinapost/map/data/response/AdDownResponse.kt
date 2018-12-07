package com.suntray.chinapost.map.data.response

/**
 *
 *  @Title ${name}
 *  @ProjectName ChinaPost
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/721:03
 *
 */
class AdDownResponse {
    var pointName=""
    var taskId=0
    var equLocation=""
    var zoneAddress=""
    var cityName=""
    var equId=""
    var equSpecify=""
    var imageList:ArrayList<image>?=null


    /**
     * 图片的内部类
     */
    inner class image{
        var imgPath="" //图片的路径
    }
}