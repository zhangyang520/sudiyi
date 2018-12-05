package com.suntray.chinapost.map.data.respository

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.map.data.api.OtherApi
import com.suntray.chinapost.map.data.request.UpdateRequest
import com.suntray.chinapost.map.data.response.UpdateResponse
import retrofit2.http.Body
import rx.Observable
import javax.inject.Inject

/**
 *  其他公共模块的 仓库
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/8 16:29
 */
class OtherRespository @Inject constructor(){

    fun update(updateRequest: UpdateRequest):Observable<BaseResp<UpdateResponse>>{
        return RetrofitFactory.instance.create(OtherApi::class.java).update(updateRequest)
    }
}