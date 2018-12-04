package com.suntray.chinapost.map.service

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.map.data.request.UpdateRequest
import com.suntray.chinapost.map.data.response.UpdateResponse
import rx.Observable

/**
 *  其他服务的接口
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/8 16:31
 */
interface OtherService {
    /**
     * 更新的服务
     */
    fun update(updateRequest: UpdateRequest): Observable<UpdateResponse>
}