package com.suntray.chinapost.user.data.respository

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.user.data.api.MineDotApi
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.data.request.FindRenewDaysRequest
import com.suntray.chinapost.user.data.request.MineReservedDotRequest
import com.suntray.chinapost.user.data.request.MineXudingDotRequest
import com.suntray.chinapost.user.data.request.RelieveSaveRequest
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable
import javax.inject.Inject

/**
 *   我相关的点位 网络请求仓库
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/22 17:57
 */
class MineDotRespository  @Inject constructor(){

    /**
     * 我预定的点位
     */
    fun mineReservedDot(mineReservedDotRequest: MineReservedDotRequest): Observable<BaseResp<ArrayList<MineReservedDot>>> {
        return RetrofitFactory.instance.create(MineDotApi::class.java). mineReservedDot(mineReservedDotRequest)
    }

    /**
     * 续订保存
     */
    fun dotXuding(mineDotXudingDotRequest: MineXudingDotRequest): Observable<BaseResp<ArrayList<Object>>>{
        return RetrofitFactory.instance.create(MineDotApi::class.java). dotXuding(mineDotXudingDotRequest)
    }

    fun relieveSave( relieveSaveRequest: RelieveSaveRequest):Observable<BaseResp<ArrayList<Object>>>{
        return RetrofitFactory.instance.create(MineDotApi::class.java). relieveSave(relieveSaveRequest)
    }

    /**
     * 查询续订天数
     */
    fun findRenewDays(@Body findRenewDaysRequest: FindRenewDaysRequest):Observable<BaseResp<Int>>{
        return RetrofitFactory.instance.create(MineDotApi::class.java).findRenewDays(findRenewDaysRequest)
    }
}