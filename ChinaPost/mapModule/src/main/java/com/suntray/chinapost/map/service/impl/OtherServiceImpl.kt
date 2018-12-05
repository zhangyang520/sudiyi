package com.suntray.chinapost.map.service.impl

import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.map.data.request.UpdateRequest
import com.suntray.chinapost.map.data.response.UpdateResponse
import com.suntray.chinapost.map.data.respository.OtherRespository
import com.suntray.chinapost.map.service.OtherService
import com.suntray.chinapost.user.data.respository.ClientRespository
import rx.Observable
import javax.inject.Inject

/**
 *  其他服务的实现类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/8 16:32
 */
class OtherServiceImpl @Inject constructor():OtherService{

    @Inject
    lateinit var otherRespository: OtherRespository

    override fun update(updateRequest: UpdateRequest): Observable<UpdateResponse> {
        return otherRespository.update(updateRequest).convertData()
    }
}