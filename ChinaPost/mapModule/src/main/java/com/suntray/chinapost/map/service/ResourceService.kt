package com.suntray.chinapost.map.service

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

/**
 *   资源相关的服务
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 19:18
 */
interface ResourceService {

    /**
     * 提交一键预定
     */
    fun oneKeySubmit(oneKeySubmitRequest: OneKeySubmitRequest): Observable<OneKeyReservedResponse>


    /**
     * 获取 点位的 资源位列表数据
     */
    fun getDotOfResourceInfo(@Body dotOfResourceInfoRequest: DotOfResourceInfoRequest):Observable<DotOfResourceListResponse>


    /**
     *  提交保存
     */
    fun submitReserve(oneKeySubmitRequest: OneKeySubmitRequest):Observable<ArrayList<Object>>


    /**
     * 获取结果
     */
    fun oneKeySubmitResult(@Body oneKeySubmitRequest: OneKeySubmitRequest):Observable<OneKeyReservedResponse>


    /***
     * 资源周期的获取
     */
    fun getResourceDateSchedule(@Body resourceDateRequest: ResourceDateRequest):Observable<ResourceDateResponse>
}