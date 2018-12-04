package com.suntray.chinapost.map.data.respository

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.map.data.api.MapApi
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.map.data.request.ProvinceDotRequest
import com.suntray.chinapost.map.data.request.ProvinceRequest
import com.suntray.chinapost.map.data.request.RadiusDotRequest
import rx.Observable
import javax.inject.Inject

/**
 *   地图请求仓库
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 14:54
 */
class MapRespository @Inject constructor(){


    /**
     *  省市点位的请求
     */
    fun provinceDot(provinceDotRequest: ProvinceDotRequest): Observable<BaseResp<ArrayList<MapDot>>> {
       return RetrofitFactory.instance.create(MapApi::class.java).provinceDot(provinceDotRequest)
    }

    /**
     * 半径点位 请求
     */
    fun radiusDot(radiusDotRequest: RadiusDotRequest): Observable<BaseResp<ArrayList<MapDot>>>{
        return RetrofitFactory.instance.create(MapApi::class.java).radiusDot(radiusDotRequest)
    }


    /**
     * 省市的请求
     */
    fun province(provinceRequest: ProvinceRequest):Observable<BaseResp<ArrayList<ProvinceCity>>>{
        return RetrofitFactory.instance.create(MapApi::class.java).province(provinceRequest)
    }
}