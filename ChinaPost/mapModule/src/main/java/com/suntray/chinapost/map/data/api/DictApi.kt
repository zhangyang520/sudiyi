package com.suntray.chinapost.map.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.bean.AdStyle
import com.suntray.chinapost.baselibrary.data.bean.DistrictType
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.map.data.response.ClientDictResponse
import retrofit2.http.POST
import rx.Observable

/**
 *   字典相关的 接口api
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 15:55
 */
interface DictApi {

    /**
     *  获取当前资源位 类型 列表
     */
    @POST(value = BaseConstants.RESOURCE_LSIT)
    fun getResourceList():Observable<BaseResp<ArrayList<ResourceType>>>


    /**
     * 获取广告类型 列表
     */
    @POST(value = BaseConstants.AD_TYPE_LIST)
    fun getAdTypeList():Observable<BaseResp<ArrayList<AdStyle>>>


    /**
     * 客户相关的字典项
     */
    @POST(value = BaseConstants.CLIENT_NAME_LIST)
    fun getClientNameList():Observable<BaseResp<ClientDictResponse>>

    /**
     * 获取 小区类型 列表
     */
    @POST(value = BaseConstants.DISTRICT_TYPE_LIST)
    fun getDistrictTypeList():Observable<BaseResp<ArrayList<DistrictType>>>
}