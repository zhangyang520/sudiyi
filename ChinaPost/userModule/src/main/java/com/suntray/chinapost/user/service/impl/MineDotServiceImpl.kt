package com.suntray.chinapost.user.service.impl

import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.data.request.MineReservedDotRequest
import com.suntray.chinapost.user.data.request.MineXudingDotRequest
import com.suntray.chinapost.user.data.respository.MineDotRespository
import com.suntray.chinapost.user.service.MineDotService
import rx.Observable
import javax.inject.Inject

/**
 *  我的点位 实现类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/22 18:02
 */
class MineDotServiceImpl @Inject constructor():MineDotService{

     @Inject
     lateinit var mineDotRespository: MineDotRespository

    override fun mineReservedDot(mineReservedDotRequest: MineReservedDotRequest): Observable<ArrayList<MineReservedDot>> {
        return mineDotRespository.mineReservedDot(mineReservedDotRequest).convertData()
    }

    override fun dotXuding(mineDotXudingDotRequest: MineXudingDotRequest): Observable<ArrayList<Object>> {
        return mineDotRespository.dotXuding(mineDotXudingDotRequest).convertData()
    }
}