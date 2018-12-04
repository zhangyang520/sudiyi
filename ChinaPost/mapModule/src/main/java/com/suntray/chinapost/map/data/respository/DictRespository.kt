package com.suntray.chinapost.map.data.respository

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.map.data.api.DictApi
import com.suntray.chinapost.baselibrary.data.bean.AdStyle
import com.suntray.chinapost.baselibrary.data.bean.DistrictType
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.map.data.response.ClientDictResponse
import rx.Observable
import javax.inject.Inject

/**
 *  字典的相关数据
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 16:11
 */
class DictRespository @Inject constructor(){

    /**
     *  获取当前资源位 列表
     */
    fun getResourceList(): Observable<BaseResp<ArrayList<ResourceType>>>{
        return RetrofitFactory.instance.create(DictApi::class.java).getResourceList()
    }


    /**
     * 获取广告类型 列表
     */
    fun getAdTypeList(): Observable<BaseResp<ArrayList<AdStyle>>>{
        return RetrofitFactory.instance.create(DictApi::class.java).getAdTypeList()
    }


    /**
     * 模糊查询 客户名称
     */
    fun getClientNameList():Observable<BaseResp<ClientDictResponse>>{
        return RetrofitFactory.instance.create(DictApi::class.java).getClientNameList()
    }


    /**
     * 获取小区类型 列表
     */
    fun getDistrictTypeList():Observable<BaseResp<ArrayList<DistrictType>>>{
        return RetrofitFactory.instance.create(DictApi::class.java).getDistrictTypeList()
    }
}