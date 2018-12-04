package com.suntray.chinapost.baselibrary.data.bean

/**
 *  省市区 实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 15:00
 */
class ProvinceCity {
    var id:Int=0 //主键id
    var type:String="" //省市区类型（0省，1市，2区）
    var provinceId:Int=0//所属省份id
    var cityId:Int=0//所属城市id
    var province:String=""//省份名称
    var city:String=""//城市名称
    var district:String=""//地区名称


    override fun toString(): String {
        return "ProvinceCity(id=$id, type='$type', provinceId=$provinceId, cityId=$cityId, province='$province', city='$city', district='$district')"
    }
}