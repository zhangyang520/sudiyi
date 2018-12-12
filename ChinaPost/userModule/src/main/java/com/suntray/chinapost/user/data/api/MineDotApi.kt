package com.suntray.chinapost.user.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.data.request.FindRenewDaysRequest
import com.suntray.chinapost.user.data.request.MineReservedDotRequest
import com.suntray.chinapost.user.data.request.MineXudingDotRequest
import com.suntray.chinapost.user.data.request.RelieveSaveRequest
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 *   我相关的点位的api
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/22 17:45
 */
interface MineDotApi {

    /**
     * 我预定的点位
     */
    @POST(BaseConstants.MINE_RESERVED_DOT)
    fun mineReservedDot(@Body mineReservedDotRequest: MineReservedDotRequest):Observable<BaseResp<ArrayList<MineReservedDot>>>


    /**
     * 续订保存
     */
    @POST(BaseConstants.MINE_XUDING_DOT)
    fun dotXuding(@Body mineDotXudingDotRequest: MineXudingDotRequest):Observable<BaseResp<ArrayList<Object>>>


    /**
     * 申请取消预订 动作
     */
    @POST(BaseConstants.RELIEVE_SAVE)
    fun relieveSave(@Body relieveSaveRequest: RelieveSaveRequest):Observable<BaseResp<ArrayList<Object>>>

    /**
     * 查询续订天数
     */
    @POST(BaseConstants.FIND_RENEWS_DAYS)
    fun findRenewDays(@Body findRenewDaysRequest: FindRenewDaysRequest):Observable<BaseResp<Int>>
}