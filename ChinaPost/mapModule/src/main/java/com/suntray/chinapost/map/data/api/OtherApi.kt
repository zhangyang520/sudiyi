package com.suntray.chinapost.map.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.map.data.request.UpdateRequest
import com.suntray.chinapost.map.data.response.UpdateResponse
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 *  其他公共的 api接口
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/8 16:27
 */
interface OtherApi {

    /**
     * 更新的接口
     */
    @POST(value = BaseConstants.UPDATE_APP)
    fun update(@Body updateRequest: UpdateRequest):Observable<BaseResp<UpdateResponse>>
}