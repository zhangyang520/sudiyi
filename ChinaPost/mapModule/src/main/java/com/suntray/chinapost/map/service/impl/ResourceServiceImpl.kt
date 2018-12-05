package com.suntray.chinapost.map.service.impl

import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.map.data.request.DotOfResourceInfoRequest
import com.suntray.chinapost.map.data.request.OneKeySubmitRequest
import com.suntray.chinapost.map.data.request.ResourceDateRequest
import com.suntray.chinapost.map.data.response.DotOfResourceListResponse
import com.suntray.chinapost.map.data.response.OneKeyReservedResponse
import com.suntray.chinapost.map.data.response.ResourceDateResponse
import com.suntray.chinapost.map.data.respository.DictRespository
import com.suntray.chinapost.map.data.respository.ResourceRespository
import com.suntray.chinapost.map.service.ResourceService
import rx.Observable
import java.util.ArrayList
import javax.inject.Inject

/**
 *   资源相关的 服务实现类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 19:19
 */
class ResourceServiceImpl @Inject constructor():ResourceService{

    @Inject
    lateinit var resourceRespository: ResourceRespository

    override fun oneKeySubmit(oneKeySubmitRequest: OneKeySubmitRequest): Observable<OneKeyReservedResponse> {
        return resourceRespository.oneKeySubmit(oneKeySubmitRequest).convertData()
    }

    override fun getDotOfResourceInfo(dotOfResourceInfoRequest: DotOfResourceInfoRequest): Observable<DotOfResourceListResponse> {
        return resourceRespository.getDotOfResourceInfo(dotOfResourceInfoRequest).convertData()
    }

    override fun submitReserve(oneKeySubmitRequest: OneKeySubmitRequest): Observable<ArrayList<Object>> {
        return resourceRespository.submitReserve(oneKeySubmitRequest).convertData()
    }


    override fun oneKeySubmitResult(oneKeySubmitRequest: OneKeySubmitRequest): Observable<OneKeyReservedResponse> {
        return resourceRespository.oneKeySubmitResult(oneKeySubmitRequest).convertData()
    }

    override fun getResourceDateSchedule(resourceDateRequest: ResourceDateRequest): Observable<ResourceDateResponse> {
        return resourceRespository.getResourceDateSchedule(resourceDateRequest).convertData()
    }
}