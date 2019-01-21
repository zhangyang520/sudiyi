package com.suntray.chinapost.map.service

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.map.data.request.*
import rx.Observable

/**
 *   地图相关的服务
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 15:14
 */
interface MapService {

    /**
     *  省市点位的请求
     */
    fun provinceDot(provinceDotRequest: ProvinceDotRequest): Observable<ArrayList<MapDot>>;

    /**
     * 半径点位 请求
     */
    fun radiusDot(radiusDotRequest: RadiusDotRequest): Observable<ArrayList<MapDot>>


    /**
     * 省市的请求
     */
    fun province(provinceRequest: ProvinceRequest): Observable<ArrayList<ProvinceCity>>;

    /**
     * 查询一键预订的数量
     */
    fun findReserveNum(findReserveNumRequest: FindReserveNumRequest):Observable<Int>

    /**
     * 消息通知的数量
     */
    fun findNewNoticeCount( findNewNoticeCount: NewNoticeCountRequest):Observable<Int>;
}