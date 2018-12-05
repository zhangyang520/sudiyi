package com.suntray.chinapost.map.data.bean


/**
 *   资源点 一个资源点 -- 包含多个 资源位
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 13:09
 */
class ResourceDotLocation {
   var id:Int=0 //点位 的id
   var resourceLocation:String="" //资源位置
   var districtType:Int=0 //小区类型
   var deviceIdLocation:String=""//设备的id 所在位置
   var deviceId:String=""
   var limitType:Int=0//限制上刊类型
   var deviceSpecification:String="" //设备的规格
   var infoImage:String=""//对应的图片的src
   var wangdianName="" //网点名称
   var devicePosition="" //设备位置
   var zonename=""// 小区类型的名称
   var publishtypename="" //限制 上刊类型名称
   var cityname=""//城市名称
   var districtname=""
   var equlocation=""
   var zoneaddress=""
   var resourceAdList= emptyList<ResourceAd>()//几个的 资源位的集合
}