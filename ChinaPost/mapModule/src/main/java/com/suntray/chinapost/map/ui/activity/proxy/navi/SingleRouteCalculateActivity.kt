package com.suntray.chinapost.map.ui.activity.proxy.navi

import android.os.Bundle
import android.view.View

import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.AMapNaviViewOptions
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.services.core.LatLonPoint
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.activity.proxy.navi.inner.BaseActivity
import com.suntray.chinapost.provider.RouterPath

@Route(path = RouterPath.MapModule.POST_SINGLE_ROUTE_CALCULATE_ACTIVITY)
class SingleRouteCalculateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        mStartPoint = intent.getParcelableExtra<LatLonPoint>("startPoint")
        mEndPoint = intent.getParcelableExtra<LatLonPoint>("endPoint")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_navi)


        mAMapNaviView = findViewById(R.id.navi_view)
        mAMapNaviView!!.onCreate(savedInstanceState)
        mAMapNaviView!!.setAMapNaviViewListener(this)

        val options = AMapNaviViewOptions()
        options.tilt = 0
        mAMapNaviView!!.viewOptions = options
    }

    override fun onInitNaviSuccess() {
        super.onInitNaviSuccess()
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         * 说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         * 注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        var strategy = 0
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi!!.strategyConvert(true, false, false, false, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mAMapNavi!!.calculateDriveRoute(sList, eList, mWayPointList, strategy)

    }

    override fun onCalculateRouteSuccess(ids: IntArray) {
        super.onCalculateRouteSuccess(ids)
        mAMapNavi!!.startNavi(NaviType.GPS)
    }
}
