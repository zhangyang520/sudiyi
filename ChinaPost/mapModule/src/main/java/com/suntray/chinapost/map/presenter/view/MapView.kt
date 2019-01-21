package com.suntray.chinapost.map.presenter.view

import com.suntray.chinapost.baselibrary.data.bean.AdStyle
import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.map.data.response.UpdateResponse

/**
 *  地图的view
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 15:20
 */
interface MapView:BaseView{

    /**
     * 请求点位半径的数据
     */
    fun onRadisDotRequest(mapDot:ArrayList<MapDot>){

    }

    /**
     * 请求省市点位的请求
     */
    fun onProvinceDotRequest(mapDot:ArrayList<MapDot>){

    }

    /**
     * 省市 区域请求
     */
    fun onProvinceDotRequestError(){

    }

    /**
     * 以半径查询 请求失败
     */
    fun onRadiusDotRequestError(){

    }
    /**
     * 请求省市的数据回调
     */
    fun onProvinceCityRequest(provinceCity: ArrayList<ProvinceCity>, action: CityListAction){

    }

    /**
     * 获取 广告类型列表数据回调
     */
    fun onGetAdStyleList(adStyleList:ArrayList<AdStyle>){

    }

    /**
     * 更新 app的回调
     */
    fun onGetAppVersion(appUpdateResponse: UpdateResponse){

    }

    /**
     * 查询一键预订的数量
     */
    fun onFindReserverNumber(number: Int){

    }

    /**
     * 消息通知的数量
     */
    fun onGetNoticeNumber(count: Int){

    }

}