package com.suntray.chinapost.map.service

import com.suntray.chinapost.baselibrary.data.bean.AdStyle
import com.suntray.chinapost.baselibrary.data.bean.DistrictType
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.map.data.response.ClientDictResponse
import rx.Observable

/**
 *   字典相关的服务 接口
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 16:19
 */
interface DictService {

    /**
     *  获取当前资源位 列表
     */
    fun getResourceList(): Observable<ArrayList<ResourceType>>


    /**
     * 获取广告类型 列表
     */
    fun getAdTypeList(): Observable<ArrayList<AdStyle>>


    /**
     * 模糊查询 客户名称
     */
    fun getClientNameList():Observable<ClientDictResponse>


    /**
     * 获取小区类型 列表
     */
    fun getDistrictTypeList():Observable<ArrayList<DistrictType>>
}