package com.suntray.chinapost.user.service

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.user.data.api.MineDotApi
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.data.request.MineReservedDotRequest
import com.suntray.chinapost.user.data.request.MineXudingDotRequest
import com.suntray.chinapost.user.data.request.RelieveSaveRequest
import rx.Observable

/**
 *  我的相关点位的服务
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/22 18:01
 */
interface MineDotService {
    /**
     * 我预定的点位
     */
    fun mineReservedDot(mineReservedDotRequest: MineReservedDotRequest): Observable<ArrayList<MineReservedDot>>

    /**
     * 续订保存
     */
    fun dotXuding(mineDotXudingDotRequest: MineXudingDotRequest): Observable<ArrayList<Object>>

    /**
     * 申请取消预订接口
     */
    fun relieveSave( relieveSaveRequest: RelieveSaveRequest):Observable<ArrayList<Object>>
}