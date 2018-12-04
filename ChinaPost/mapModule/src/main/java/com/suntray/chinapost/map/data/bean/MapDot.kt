package com.suntray.chinapost.map.data.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 *  地图上对应的点
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 14:31
 */
class MapDot:Serializable{

   var id=0
   var province:Int?=-1 //省的编码
   var provincename:String=""//省的名称
   var city:Int=-1//所在城市的编码
   var cityname:String=""//城市名称
   var district:Int=-1;//区所在的编码
   var districtname:String=""//所在区的名称
   var areaname:String=""//所在省区的名称
   var belongtocity:String=""//所属市城市
   var zonetype:Int=-1//小区类型
   var zoneaddress:String=""//小区地址
   var pointid:String=""//点位的id
   var pointname:String=""//点位名称
   var equid:String=""//设备的id
   var equtype: Int = -1;//设备类型
   var equspecify:String=""//设备规格
   var equlocation:String=""//设备位置
   var longitude:String=""//设备的精度
   var latitude:String=""//设备维度
   var publishtype:String=""//广告上刊限制类型
   var ispublish:Int=0//是否可上刊广告
   var state:Int=0//点位状态
   var inoroutdoor:Int=0//室内/室外
   var figurecode:String=""//图形代码
   var shape:String=""//简位形状
   var imgpath:String=""//图片路径
   var combocode:String=""//组合编码
   var zonename=""// 小区类型的名称
   var publishtypename="" //限制 上刊类型名称


   override fun toString(): String {
      return "MapDot(id=$id, province=$province, provincename='$provincename', city=$city, cityname='$cityname', district=$district, districtname='$districtname', areaname='$areaname', belongtocity='$belongtocity', zonetype=$zonetype, zoneaddress='$zoneaddress', pointid='$pointid', pointname='$pointname', equid='$equid', equtype=$equtype, equspecify='$equspecify', equlocation='$equlocation', longitude='$longitude', latitude='$latitude', publishtype='$publishtype', ispublish=$ispublish, state=$state, inoroutdoor=$inoroutdoor, figurecode='$figurecode', shape='$shape', imgpath='$imgpath', combocode='$combocode', zonename='$zonename', publishtypename='$publishtypename')"
   }
}