package com.suntray.chinapost.map.service.impl

import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.baselibrary.data.bean.AdStyle
import com.suntray.chinapost.baselibrary.data.bean.DistrictType
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.map.data.response.ClientDictResponse
import com.suntray.chinapost.map.data.respository.DictRespository
import com.suntray.chinapost.map.service.DictService
import rx.Observable
import javax.inject.Inject

/**
 *   字典相关服务的实现类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 16:22
 */
class DictServiceImpl @Inject constructor():DictService{

    @Inject
    lateinit var dictRespository: DictRespository

    override fun getResourceList(): Observable<ArrayList<ResourceType>> {
        return dictRespository.getResourceList().convertData()
    }

    override fun getAdTypeList(): Observable<ArrayList<AdStyle>> {
        return dictRespository.getAdTypeList().convertData()
    }

    override fun getClientNameList(): Observable<ClientDictResponse> {
        return dictRespository.getClientNameList().convertData()
    }

    override fun getDistrictTypeList(): Observable<ArrayList<DistrictType>> {
        return dictRespository.getDistrictTypeList().convertData()
    }
}