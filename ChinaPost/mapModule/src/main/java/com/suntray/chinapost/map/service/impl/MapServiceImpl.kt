package com.suntray.chinapost.map.service.impl

import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.map.data.request.ProvinceDotRequest
import com.suntray.chinapost.map.data.request.ProvinceRequest
import com.suntray.chinapost.map.data.request.RadiusDotRequest
import com.suntray.chinapost.map.data.respository.MapRespository
import com.suntray.chinapost.map.service.MapService
import rx.Observable
import javax.inject.Inject

/**
 *   地图相关的服务
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 15:16
 */
class MapServiceImpl @Inject constructor(): MapService {

    @Inject
    lateinit var mapRespository: MapRespository

    override fun provinceDot(provinceDotRequest: ProvinceDotRequest): Observable<ArrayList<MapDot>> {
        return mapRespository.provinceDot(provinceDotRequest).convertData()
    }

    override fun radiusDot(radiusDotRequest: RadiusDotRequest): Observable<ArrayList<MapDot>> {
        return mapRespository.radiusDot(radiusDotRequest).convertData()
    }

    override fun province(provinceRequest: ProvinceRequest): Observable<ArrayList<ProvinceCity>> {
        return mapRespository.province(provinceRequest).convertData()
    }
}