package com.suntray.chinapost.map.ui.activity.proxy.navi

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route

import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.NaviLatLng
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.activity.proxy.navi.inner.BaseActivity
import com.suntray.chinapost.provider.RouterPath

/**
 * 骑行导航的界面
 */
@Route(path = RouterPath.MapModule.POST_RIDE_ROUTE_CALCULATE_ACTIVITY)
class RideRouteCalculateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_basic_navi)
        mAMapNaviView = findViewById(R.id.navi_view) as AMapNaviView
        mAMapNaviView!!.onCreate(savedInstanceState)
        mAMapNaviView!!.setAMapNaviViewListener(this)
    }

    override fun onInitNaviSuccess() {
        super.onInitNaviSuccess()
        mAMapNavi!!.calculateRideRoute(NaviLatLng(39.925846, 116.435765), NaviLatLng(39.925846, 116.532765))
    }

    override fun onCalculateRouteSuccess(ids: IntArray) {
        super.onCalculateRouteSuccess(ids)
        mAMapNavi!!.startNavi(NaviType.GPS)
    }
}
