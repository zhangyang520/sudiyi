package com.suntray.chinapost.map.data.respository

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.map.data.api.ResourceApi
import com.suntray.chinapost.map.data.request.DotOfResourceInfoRequest
import com.suntray.chinapost.map.data.request.OneKeySubmitRequest
import com.suntray.chinapost.map.data.request.ResourceDateRequest
import com.suntray.chinapost.map.data.response.DotOfResourceListResponse
import com.suntray.chinapost.map.data.response.OneKeyReservedResponse
import com.suntray.chinapost.map.data.response.ResourceDateResponse
import retrofit2.http.Body
import rx.Observable
import java.util.ArrayList
import javax.inject.Inject

/**
 *  资源 相关的操作仓库
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 19:16
 */
class ResourceRespository @Inject constructor(){


    /**
     * 单个 点击 预定
     */
    fun oneKeySubmit(@Body oneKeySubmitRequest: OneKeySubmitRequest): Observable<BaseResp<OneKeyReservedResponse>>{
        return  RetrofitFactory.instance.create(ResourceApi::class.java).oneKeySubmit(oneKeySubmitRequest)
    }

    fun getDotOfResourceInfo(@Body dotOfResourceInfoRequest: DotOfResourceInfoRequest):Observable<BaseResp<DotOfResourceListResponse>>{
        return  RetrofitFactory.instance.create(ResourceApi::class.java).getDotOfResourceInfo(dotOfResourceInfoRequest)
    }

    /**
     * 提交保存
     */
    fun submitReserve(@Body oneKeySubmitRequest: OneKeySubmitRequest):Observable<BaseResp<ArrayList<Object>>>{
        return  RetrofitFactory.instance.create(ResourceApi::class.java).submitReserve(oneKeySubmitRequest)
    }

    /**
     * 获取结果
     */
    fun oneKeySubmitResult(@Body oneKeySubmitRequest: OneKeySubmitRequest):Observable<BaseResp<OneKeyReservedResponse>>{
        return  RetrofitFactory.instance.create(ResourceApi::class.java).oneKeySubmitResult(oneKeySubmitRequest)
    }

    fun getResourceDateSchedule(@Body resourceDateRequest: ResourceDateRequest):Observable<BaseResp<ResourceDateResponse>>{
        return  RetrofitFactory.instance.create(ResourceApi::class.java).getResourceDateSchedule(resourceDateRequest)
    }
}