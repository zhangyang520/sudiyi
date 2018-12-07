package com.suntray.chinapost.map.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.map.data.request.DotOfResourceInfoRequest
import com.suntray.chinapost.map.data.request.FindResourceReportRequest
import com.suntray.chinapost.map.data.request.OneKeySubmitRequest
import com.suntray.chinapost.map.data.request.ResourceDateRequest
import com.suntray.chinapost.map.data.response.AdDownResponse
import com.suntray.chinapost.map.data.response.DotOfResourceListResponse
import com.suntray.chinapost.map.data.response.OneKeyReservedResponse
import com.suntray.chinapost.map.data.response.ResourceDateResponse
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable
import java.util.*

/**
 *   资源位  相关的接口
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 18:53
 */
interface ResourceApi {

    /**
     * 提交一键预定的接口
     */
    @POST(value = BaseConstants.ONE_KEY_SUBMIT)
    fun oneKeySubmit(@Body oneKeySubmitRequest: OneKeySubmitRequest):Observable<BaseResp<OneKeyReservedResponse>>

    /**
     *  获取 点位的 资源位 数据
     */
    @POST(value = BaseConstants.DOT_OF_RESOURCE_LIST)
    fun getDotOfResourceInfo(@Body dotOfResourceInfoRequest: DotOfResourceInfoRequest):Observable<BaseResp<DotOfResourceListResponse>>

    /**
     *  获取上下刊的内容
     */
    @POST(value = BaseConstants.FindResourceReport)
    fun getResourceReportRequest(@Body findResourceReportRequest: FindResourceReportRequest):Observable<BaseResp<AdDownResponse>>
    /**
     * 获取结果
     */
    @POST(value = BaseConstants.ONE_KEY_SAVE_SUBMIT_RESULT)
    fun oneKeySubmitResult(@Body oneKeySubmitRequest: OneKeySubmitRequest):Observable<BaseResp<OneKeyReservedResponse>>

    /**
     * 点位预定的保存
     */
    @POST(value = BaseConstants.SAVE_SUBMIT_RESERVE)
    fun submitReserve(@Body oneKeySubmitRequest: OneKeySubmitRequest):Observable<BaseResp<ArrayList<Object>>>;


    /**
     * 获取 资源的周期
     */
    @POST(value = BaseConstants.RESOURCE_DATE)
    fun getResourceDateSchedule(@Body resourceDateRequest: ResourceDateRequest):Observable<BaseResp<ResourceDateResponse>>;
}