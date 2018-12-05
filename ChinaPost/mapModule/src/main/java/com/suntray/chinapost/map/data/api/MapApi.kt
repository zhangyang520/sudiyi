package com.suntray.chinapost.map.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.map.data.request.ProvinceDotRequest
import com.suntray.chinapost.map.data.request.ProvinceRequest
import com.suntray.chinapost.map.data.request.RadiusDotRequest
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable


/**
 *   地图相关的api
 *   @Author 张扬 @version 1.0
 *   @Date 2018/9/19 14:30
 */
interface MapApi {

    /**
     *  省市区 查询接口
     */
    @POST(value = BaseConstants.PROVINCE_DOT)
    fun provinceDot(@Body provinceDotRequest: ProvinceDotRequest):Observable<BaseResp<ArrayList<MapDot>>>

    /**
     * 省市区 接口
     */
    @POST(value = BaseConstants.PROVINCE)
    fun province(@Body province: ProvinceRequest):Observable<BaseResp<ArrayList<ProvinceCity>>>

    /**
     *  半径点位接口
     */
    @POST(value = BaseConstants.RADIUS_DOT)
    fun radiusDot(@Body radiusDotRequest: RadiusDotRequest):Observable<BaseResp<ArrayList<MapDot>>>
}